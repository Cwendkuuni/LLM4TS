import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.impl.BaseExtension;
import org.pf4j.impl.ModuleLayerFactory;
import org.pf4j.impl.util.ClassLoaderUtils;
import org.pf4j.impl.util.ReflectionUtils;
import org.pf4j.test.util.TestExtension;

import java.sql.Connection;
import java.sql.SQLException;

@TestExtension
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws Exception {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Class<?> clazz = ClassLoaderUtils.loadClass(Extension.class, this.getClass().getClassLoader());
        BaseExtension baseExtension = (BaseExtension) ReflectionUtils.newInstance(clazz, extension);
        ModuleLayerFactory moduleLayerFactory = baseExtension.getModuleLayerFactory();
        String connectionId = "test_connection_id";
        Connection connection = null; // Provide a valid Connection here

        extension.killQuery(connection, connectionId);

        Assertions.assertDoesNotThrow(() -> JdbcOperationsUtil.getJdbcOperations(connection).execute("SELECT 1 FROM DUAL WHERE ID = " + connectionId), "Query should be killed");
    }

    // Edge case: Test with an invalid connection
    @Test
    public void testKillQueryWithInvalidConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = null; // Provide a null or invalid Connection here
        String connectionId = "test_connection_id";

        Assertions.assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId), "Should throw an SQLException with an invalid Connection");
    }
}