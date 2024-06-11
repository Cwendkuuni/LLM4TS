import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    EnvironmentService environmentService;

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkSqlsTest() {
        Long environmentId = 1L;
        String db = "testDb";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig(); // assuming it has a constructor with no params

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, db, sqls, config);

        Assert.assertTrue(checkViolations.isEmpty()); // Depending on your business logic, it might be assertFalse here
    }
}