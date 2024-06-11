import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testValidParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE my_table ADD COLUMN new_column INT;");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.TABLE_SWAP);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
 params.setDelimiter(";");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 params.setLockUsers(lockUsers);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(30, params.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.TABLE_SWAP, params.getSwapTableType());
 assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertFalse(params.isLockUsersEmpty());
 assertEquals("user1", params.getLockUsers().get(0));
 }

 @Test
 public void testEmptyParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 assertNull(params.getSqlType());
 assertNull(params.getSqlContent());
 assertNull(params.getErrorStrategy());
 assertNull(params.getLockTableTimeOutSeconds());
 assertNull(params.getSwapTableType());
 assertNull(params.getOriginTableCleanStrategy());
 assertNull(params.getDelimiter());
 assertTrue(params.isLockUsersEmpty());
 }

 @Test(expected = UnexpectedException.class)
 public void testInvalidSqlContent() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE my_table ADD COLUMN new_column INT;\nINVALID_SQL;");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.TABLE_SWAP);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
 params.setDelimiter(";");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 params.setLockUsers(lockUsers);
 }
}