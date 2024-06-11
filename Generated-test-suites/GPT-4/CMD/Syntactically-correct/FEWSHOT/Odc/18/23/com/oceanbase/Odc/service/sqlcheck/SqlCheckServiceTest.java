
package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Assert;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckBySession() {
        // Arrange
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM table_name;");
        sqlCheckReq.setDelimiter(";");

        SqlCheckService sqlCheckService = new SqlCheckService();
        // Act
        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);
        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testCheckByEnvironment() {
        // Arrange
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table_name;", 0));

        SqlCheckService sqlCheckService = new SqlCheckService();
        // Act
        List<CheckViolation> result = sqlCheckService.check(1L, "databaseName", sqls, connectionConfig);
        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetRulesBySession() {
        // Arrange
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        SqlCheckService sqlCheckService = new SqlCheckService();
        // Act
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);
        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetRulesByJdbc() {
        // Arrange
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        SqlCheckService sqlCheckService = new SqlCheckService();
        // Act
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, null);
        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Arrange
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation());

        SqlCheckService sqlCheckService = new SqlCheckService();
        // Act
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        // Assert
        Assert.assertNotNull(result);
    }
}
