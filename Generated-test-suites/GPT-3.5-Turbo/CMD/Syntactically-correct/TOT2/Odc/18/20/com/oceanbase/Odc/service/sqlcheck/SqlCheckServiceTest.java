import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckSession() {
        // Mocking data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        // Mocking behavior
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        // Testing the method
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Validating the result
        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckEnvironment() {
        // Mocking data
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();

        // Mocking behavior
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(any(Long.class))).thenReturn(env);
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        // Mocking JdbcTemplate
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class))).thenReturn("");

        // Testing the method
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Validating the result
        assertNotNull(checkViolations);
        assertTrue(checkViolations.isEmpty());
    }

    // Add more test cases for other public methods in SqlCheckService if needed

}