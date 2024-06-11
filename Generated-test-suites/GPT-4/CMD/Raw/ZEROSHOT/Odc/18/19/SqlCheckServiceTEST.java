
package com.oceanbase.odc.service.sqlcheck;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    ConnectionConfig connectionConfig;

    @Test
    public void testCheckWithConnectionSession() {
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertThat(checkResults, is(notNullValue()));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM sampleTable"), new OffsetString("INSERT INTO sampleTable VALUES ('Data1')"));
        Environment env = mock(Environment.class);

        when(environmentService.detail(anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "dbName", sqls, connectionConfig);
        assertThat(checkViolations, is(notNullValue()));
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = Collections.singletonList(new Rule());

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
       
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertThat(sqlCheckRules, is(notNullValue()));
    }

    @Test
    public void testGetRulesWithDialectType() {
        List<Rule> rules = Collections.singletonList(new Rule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, mock(JdbcOperations.class));
        assertThat(sqlCheckRules, is(notNullValue()));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertThat(violatedRules, is(notNullValue()));
    }
}
