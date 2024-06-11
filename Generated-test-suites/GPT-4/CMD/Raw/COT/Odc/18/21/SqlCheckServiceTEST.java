
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import com.oceanbase.odc.service.sqlcheck.*;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {
    private ConnectionSession session;
    private SqlCheckReq req;
    private Long environmentId;
    private String databaseName;
    private List<OffsetString> sqls;
    private ConnectionConfig config;

    @Before
    public void setUp() throws Exception {
        session = new ConnectionSession();
        req = new SqlCheckReq();
        environmentId = 1L;
        databaseName = "testDatabase";
        sqls = new ArrayList<OffsetString>();
        config = new ConnectionConfig();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCheckWithSessionAndReq() {
        SqlCheckService service = new SqlCheckService();
        List<CheckResult> results = service.check(this.session, this.req);
        assertNotNull(results);
    }

    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsAndConfig() {
        SqlCheckService service = new SqlCheckService();
        List<CheckViolation> checkViolations = service.check(this.environmentId, this.databaseName, this.sqls, this.config);
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithRulesAndSession() {
        SqlCheckService service = new SqlCheckService();
        List<Rule> rules = new ArrayList<Rule>();
        List<SqlCheckRule> sqlCheckRules = service.getRules(rules, this.session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithRulesDialectTypeAndJdbc() {
        SqlCheckService service = new SqlCheckService();
        List<Rule> rules = new ArrayList<Rule>();
        DialectType dialectType = DialectType.ANONYMOUS;
        JdbcOperations jdbc = new JdbcTemplate();
        List<SqlCheckRule> sqlCheckRules = service.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService service = new SqlCheckService();
        List<Rule> rules = new ArrayList<Rule>();
        List<CheckViolation> checkViolations = new ArrayList<CheckViolation>();
        rules = service.fullFillRiskLevel(rules, checkViolations);
        assertNotNull(rules);
    }
}
