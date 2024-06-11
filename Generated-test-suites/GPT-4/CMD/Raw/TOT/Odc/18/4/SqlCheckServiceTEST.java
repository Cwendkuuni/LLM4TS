package com.oceanbase.odc.service.sqlcheck;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
   @Autowired
   private SqlCheckService sqlCheckService;

   @Test
   public void testCheck() {
       // Setup
       List<SqlCheckRule> sqlCheckRules = Collections.emptyList(); 
       Mockito.when(ruleService.list(any(), any())).thenReturn(sqlCheckRules);
    
       // Execute
       ConnectionSession session = mock(ConnectionSession.class);
       SqlCheckReq req = mock(SqlCheckReq.class);
       List<CheckResult> results = sqlCheckService.check(session, req);

       // Assert
       assertNotNull(results);
       assertTrue(results.isEmpty());
   }

   @Test
   public void testCheckWithEnvironmentId() {
       // Setup
       List<SqlCheckRule> sqlCheckRules = Collections.emptyList(); 
       Mockito.when(ruleService.list(any(), any())).thenReturn(sqlCheckRules);
    
       // Execute
       List<OffsetString> sqls = Arrays.asList(mock(OffsetString.class)); 
       ConnectionConfig config = mock(ConnectionConfig.class);
       List<CheckViolation> results = sqlCheckService.check(1L, "testDB", sqls, config);

       // Assert
       assertNotNull(results);
       assertTrue(results.isEmpty());
   }

   @Test
   public void testGetRules() {
       // Setup
       JdbcOperations jdbc = mock(JdbcOperations.class);
       DialectType dialectType = DialectType.MYSQL;
       List<Rule> rules = Collections.singletonList(mock(Rule.class));

       // Execute
       List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbc);

       // Assert
       Assert.assertNotNull(result);
   }

   @Test
   public void testFullFillRiskLevel() {
       // Setup
       CheckViolation violation = mock(CheckViolation.class);
       List<CheckViolation> violations = Collections.singletonList(violation);
       Rule rule = mock(Rule.class);
       List<Rule> rules = Collections.singletonList(rule);

       // Execute
       List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

       // Assert
       Assert.assertNotNull(result);
   }

}