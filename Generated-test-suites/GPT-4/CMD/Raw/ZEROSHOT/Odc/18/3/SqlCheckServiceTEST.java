
package com.oceanbase.odc.service.sqlcheck;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.validation.Valid;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = Mockito.mock(SqlCheckReq.class);

        when(req.getScriptContent()).thenReturn("test_content");
        when(req.getDelimiter()).thenReturn(";");

        List<CheckResult> checkResults = sqlCheckService.check(session,req);

        verify(ruleService, times(1)).list(Mockito.anyLong(), Mockito.any());
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("test_sql", 1));

        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_db", sqls, config);

        verify(environmentService, times(1)).detail(Mockito.anyLong());
        verify(ruleService, times(1)).list(Mockito.anyLong(), Mockito.any());
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, violatedRules.size());
    }
}
