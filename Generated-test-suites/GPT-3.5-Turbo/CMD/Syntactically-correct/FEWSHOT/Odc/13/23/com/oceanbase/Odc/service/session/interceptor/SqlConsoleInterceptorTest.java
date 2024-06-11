//import statements

public class SqlConsoleInterceptorTest {

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private RuleService ruleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        // test case 1
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result); // assert result is true

        // test case 2
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);

        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertFalse(result); // assert result is false
    }

    @Test
    public void testDoAfterCompletion() {
        // test case 1
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport()); // assert allowExport is false

        // test case 2
        response.setStatus(SqlExecuteStatus.FAILURE);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertNull(response.getResultSetMetaData()); // assert resultSetMetaData is null
    }

    @Test
    public void testIsIndividualTeam() {
        // test case 1
        User user = new User(OrganizationType.INDIVIDUAL);

        when(authenticationFacade.currentUser()).thenReturn(user);

        assertTrue(sqlConsoleInterceptor.isIndividualTeam()); // assert true for individual team

        // test case 2
        user.setOrganizationType(OrganizationType.TEAM);

        assertFalse(sqlConsoleInterceptor.isIndividualTeam()); // assert false for team
    }

    @Test
    public void testDetermineSqlType() {
        // test case 1
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertNotNull(result); // assert result is not null
    }

}