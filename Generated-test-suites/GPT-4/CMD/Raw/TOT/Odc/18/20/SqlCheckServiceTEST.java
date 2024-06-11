
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SqlCheckService.class})
public class SqlCheckServiceTest {

    @Autowired
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck_method1(){
        ConnectionSession session = new ConnectionSession(); // fill necessary data
        SqlCheckReq req = new SqlCheckReq(); // fill necessary data

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
        // Add more assertions based on what you expect
    }

    @Test
    public void testCheck_method2(){
        Long environmentId = 1L; 
        String databaseName = "myDB"; 
        List<OffsetString> sqls = new ArrayList<OffsetString>();
        ConnectionConfig config = new ConnectionConfig(); // fill necessary data

        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(results);
        // Add more assertions based on what you expect

    }

    // Add other methods' tests...
}
