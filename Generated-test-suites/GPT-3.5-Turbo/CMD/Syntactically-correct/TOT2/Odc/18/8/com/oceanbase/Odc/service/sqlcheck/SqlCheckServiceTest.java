import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithValidParameters() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM TABLE", ";");

        // Act
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCheckWithNullSession() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM TABLE", ";");

        // Act
        List<CheckResult> result = sqlCheckService.check(null, req);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckWithNullRequest() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = mock(ConnectionSession.class);

        // Act
        List<CheckResult> result = sqlCheckService.check(session, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq("", ";");

        // Act
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithNullRules() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();

        // Act
        List<SqlCheckRule> result = sqlCheckService.getRules(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithValidRules() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        
        // Act
        List<SqlCheckRule> result = sqlCheckService.getRules(rules);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithNullRules() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        // Act
        List<Rule> result = sqlCheckService.fullFillRiskLevel(null, violations);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithValidRules() {
        // Arrange
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        // Act
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}