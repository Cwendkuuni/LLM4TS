import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectResultTest {

  @Test
  public void testDefaultConstructor() {
    ObjectResult objectResult = new ObjectResult();
    assertNull(objectResult.getName());
    assertNull(objectResult.getSchema());
    assertNull(objectResult.getType());
    assertNull(objectResult.getStatus());
    assertNull(objectResult.getCount());
    assertNull(objectResult.getTotal());
  }

  @Test
  public void testConstructorWithThreeArguments() {
    ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
    assertEquals("schema1", objectResult.getSchema());
    assertEquals("name1", objectResult.getName());
    assertEquals("type1", objectResult.getType());
    assertNull(objectResult.getStatus());
    assertNull(objectResult.getCount());
    assertNull(objectResult.getTotal());
  }

  @Test
  public void testStaticFactoryMethod() {
    ObjectStatus objectStatusMock = Mockito.mock(ObjectStatus.class);
    ObjectResult objectResult = ObjectResult.of(objectStatusMock);
    assertEquals(objectStatusMock.getType(), objectResult.getType());
    assertEquals(objectStatusMock.getStatus(), objectResult.getStatus());
    assertEquals(objectStatusMock.getName(), objectResult.getName());
    assertEquals(objectStatusMock.getSchema(), objectResult.getSchema());
    assertEquals(objectStatusMock.getCount().get(), objectResult.getCount().get());
    assertEquals(objectStatusMock.getTotal().get(), objectResult.getTotal().get());
  }

  @Test
  public void testGetSummary() {
    ObjectResult objectResult = new ObjectResult();
    objectResult.setSchema("schema1");
    objectResult.setName("name1");
    objectResult.setType("type1");
    String expectedSummary = "schema1.name1[type1]";
    assertEquals(expectedSummary, objectResult.getSummary());
  }

  @Test
  public void testGetExportPaths() {
    ObjectResult objectResult = new ObjectResult();
    List<URL> exportPaths = Arrays.asList(new URL("http://test.com/export1"), new URL("http://test.com/export2"));
    objectResult.setExportPaths(exportPaths);
    assertEquals(exportPaths, objectResult.getExportPaths());
  }
}