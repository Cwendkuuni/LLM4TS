
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.core.sql.split.OffsetString;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {
    
    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession connectionSession;
    
    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCheckWithSessionAndReq() {
        SqlCheckReq req = new SqlCheckReq();
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcTemplate.class));
        List<Rule> ruleList = Arrays.asList(new Rule());
        when(ruleService.list(anyLong(), any())).thenReturn(ruleList);
        List<CheckResult> result = sqlCheckService.check(connectionSession, req);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsAndConfig() {
        List<OffsetString> sqls = new ArrayList<>();
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<Rule>());
        List<CheckViolation> result = sqlCheckService.check(1L, "testDatabase", sqls, connectionConfig);
        assertEquals(0, result.size());
    }
    
   @Test
   public void testGetRulesWithRulesSession() {
       when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
       when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcTemplate.class));
       List<Rule> ruleList = Arrays.asList(new Rule());
       List<SqlCheckRule> result = sqlCheckService.getRules(ruleList, connectionSession);
       assertEquals(1, result.size());
   }
   
   @Test
   public void testGetRulesWithRulesDialectTypeAndJdbc() {
       List<Rule> ruleList = Arrays.asList(new Rule());
       List<SqlCheckRule> result = sqlCheckService.getRules(ruleList, DialectType.ORACLE, mock(JdbcTemplate.class));
       assertEquals(1, result.size());
   }
   
   @Test
   public void testFullFillRiskLevel() {
       List<Rule> ruleList = Arrays.asList(new Rule());
       List<CheckViolation> violations = Arrays.asList(new CheckViolation());
       List<Rule> result = sqlCheckService.fullFillRiskLevel(ruleList, violations);
       assertEquals(1, result.size());
   }
}
