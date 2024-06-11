import static org.mockito.Mockito.*;
    import static org.junit.Assert.*;
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.MockitoJUnitRunner;
    import java.util.*;
    import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
    import com.oceanbase.odc.service.regulation.ruleset.RuleService;
    import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
    import com.oceanbase.odc.core.session.ConnectionSession;
    import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
    import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

    @RunWith(MockitoJUnitRunner.class)
    public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    private Long sessionId = 1L;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        sqlCheckReq = mock(SqlCheckReq.class);
    }

    @Test
    public void testCheck_ReturnsEmptyResult() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertTrue(sqlCheckService.check(session, sqlCheckReq).isEmpty());
    }

    @Test
    public void testCheck_ReturnsResults() {

        List<Rule> ruleList = new ArrayList<>();
        Rule rule = mock(Rule.class);
        ruleList.add(rule);

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(sessionId);
        when(ruleService.list(anyLong(), any())).thenReturn(ruleList);

        assertFalse(sqlCheckService.check(session, sqlCheckReq).isEmpty());
    }

    @Test
    public void testGetRules_ReturnsEmptyList() {
        List<Rule> ruleList = new ArrayList<>();

        assertTrue(sqlCheckService.getRules(ruleList, session).isEmpty());
    }

    @Test
    public void testGetRules_ReturnsRuleList() {

        List<Rule> ruleList = new ArrayList<>();
        Rule rule = mock(Rule.class);
        ruleList.add(rule);

        assertFalse(sqlCheckService.getRules(ruleList, session).isEmpty());
    }

    //Additional methods for checking violations, environment and other methods go here
    }