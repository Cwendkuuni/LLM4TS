/*
 * This file is generated as part of the test scripts for the SqlCheckService class in the Oceanbase project.
 */

import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.sqlcheck.*;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import java.util.*;

/*
 * SqlCheckServiceTest
 */
@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession session;
    @Mock
    private SqlCheckReq req;
    @Mock
    private QueryRuleMetadataParams params;
    @Mock
    private ConnectionConfig config;


    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(session.getDialectType()).thenReturn(DialectType._NULL);
        when(config.getDialectType()).thenReturn(DialectType._NULL);
    }

    @Test
    public void testCheckWithConnectionSession() {
        sqlCheckService.check(session, req);
        verify(ruleService, times(1)).list(anyLong(), any());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(
                "select * from test_table;", 0
        ));

        sqlCheckService.check(1L, "test_db", sqls, config);
        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(anyLong(), any());
    }

    @Test
    public void testGetRulesForSession() {
        sqlCheckService.getRules(new ArrayList<>(), session);
        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    @Test
    public void testGetRulesForJdbcOperations() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        sqlCheckService.getRules(new ArrayList<>(), DialectType._NULL, jdbcOperations);
        //No verification to be done as the returned list is empty
    }
    
    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(CheckViolationType.OBJECT_NOT_FOUND);
        violations.add(violation);
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("${OBJECT_NOT_FOUND}");
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        sqlCheckService.fullFillRiskLevel(rules, violations);

        assert violation.getLevel() == rule.getLevel();
    }
}