
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService service;

    @Mock
    private ConnectionSession session;
    
    @Mock
    private SqlCheckReq req;

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;

    @Test
    public void testCheck() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(session.getRuleSetId()).thenReturn(1L);
        Mockito.when(req.getScriptContent()).thenReturn("SELECT * FROM table;");
        Mockito.when(req.getDelimiter()).thenReturn(";");
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
            .thenReturn(Collections.singletonList(new Rule()));
        
        List<CheckResult> result = service.check(session, req);
        assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsAndConfig() {
        MockitoAnnotations.initMocks(this);

        Environment env = new Environment();
        env.setRulesetId(1L);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        config.setDatabaseName("testDB");

        OffsetString sql = new OffsetString("SELECT * FROM table;", 0);
        List<OffsetString> sqls = Arrays.asList(sql);

        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
            .thenReturn(Collections.singletonList(new Rule()));

        List<CheckViolation> violations = service.check(1L, "testDB", sqls, config);
        assertNotNull(violations);
    }
}   
