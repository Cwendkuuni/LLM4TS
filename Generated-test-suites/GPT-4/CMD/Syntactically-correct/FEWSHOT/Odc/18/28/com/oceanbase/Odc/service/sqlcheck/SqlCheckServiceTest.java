package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;


@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Test
    public void testGetRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        when(session.getDialectType()).thenReturn(DialectType.DEFAULT);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        CheckViolation violation = new CheckViolation();
        violation.setType(new CheckViolationType("Test"));
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(result.size(), violations.size());
    }

    @Test
    public void testCheck() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table;"));
        ConnectionConfig config = new ConnectionConfig();

        when(environmentService.detail(environmentId)).thenReturn(new Environment());

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(result.size(), sqls.size());
    }

    @Test
    public void testEmptyCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertTrue(result.isEmpty());
    }

}