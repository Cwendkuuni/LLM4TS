package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

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
    private ConnectionConfig config;

    @Mock
    private JdbcOperations jdbc;

    @Mock
    private SingleConnectionDataSource dataSource;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheckWithSessionAndReq() {
      MockitoAnnotations.initMocks(this);

      Mockito.when(req.getDelimiter()).thenReturn(";");
      Mockito.when(req.getScriptContent()).thenReturn(Arrays.asList(new OffsetString("select * from table",0)));
      Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);

      List<CheckResult> result = sqlCheckService.check(session, req);

      assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentIdDatabaseAndSQLs() {
      MockitoAnnotations.initMocks(this);

      Mockito.when(environmentService.detail(1L)).thenReturn(null);
      Mockito.when(config.getDialectType()).thenReturn(DialectType.MYSQL);

      List<CheckViolation> result = sqlCheckService.check(1L, "test_database", 
          Arrays.asList(new OffsetString("select * from table",0)),
          config);

      assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithSession() {
      MockitoAnnotations.initMocks(this);

      Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
      Mockito.when(session.getSyncJdbcExecutor("console_ds")).thenReturn(jdbc);

      List<SqlCheckRule> result = sqlCheckService.getRules(new ArrayList<>(), session);

      assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
      MockitoAnnotations.initMocks(this);

      Mockito.when(config.getDialectType()).thenReturn(DialectType.MYSQL);
      Mockito.when(session.getSyncJdbcExecutor("console_ds")).thenReturn(jdbc);

      List<SqlCheckRule> result = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbc);

      assertTrue(result.isEmpty());
    }
}