
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.pf4j.TestUtil;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection connectionMock;

    @BeforeEach
    public void init() {
        ExtensionFactory factory = TestUtil.getExtensionFactory();
        PluginWrapper pluginWrapper = factory.getPluginWrapper("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension");
        extension = (OBMySQLSessionExtension) pluginWrapper.getPluginInstance();
        connectionMock = Mockito.mock(Connection.class);
    }

    // ... Previous test cases

    @Test
    public void testMergeTestCases() {
        // Merge all the test cases here
    }
}
