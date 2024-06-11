import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectStatus = new ObjectStatus();
        objectResult = new ObjectResult();
    }

    // Test constructor
    @Test
    public void testObjectResultConstruction() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    // Test static method of
    @Test
    public void testObjectResultOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    // Test getSummary method
    @Test
    public void testGetSummary() {
        objectResult.setSchema("schema1");
        objectResult.setName("obj1");
        objectResult.setType("type1");
        String expectedSummary = "schema1.obj1[type1]";
        String actualSummary = objectResult.getSummary();
        assertEquals(expectedSummary, actualSummary);
    }

    // Test empty constructor and getters
    @Test
    public void testEmptyConstructorAndGetters() {
        ObjectResult result = new ObjectResult();
        assertNull(result.getSchema());
        assertNull(result.getName());
        assertNull(result.getType());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    // Test setters
    @Test
    public void testSetters() {
        objectResult.setSchema("schema2");
        objectResult.setName("obj2");
        objectResult.setType("type2");
        objectResult.setStatus(ObjectStatus.STATUS_OK);
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
        assertEquals("schema2", objectResult.getSchema());
        assertEquals("obj2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals(ObjectStatus.STATUS_OK, objectResult.getStatus());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    // Test exportPaths getter and setter
    @Test
    public void testExportPaths() {
        List<URL> exportPaths = Arrays.asList(new URL("path1"), new URL("path2"));
        objectResult.setExportPaths(exportPaths);
        List<URL> actualExportPaths = objectResult.getExportPaths();
        assertEquals(exportPaths, actualExportPaths);
        assertEquals(2, actualExportPaths.size());
        assertEquals("path1", actualExportPaths.get(0).getFile());
        assertEquals("path2", actualExportPaths.get(1).getFile());
    }

    // Test empty exportPaths getter and setter
    @Test
    public void testEmptyExportPaths() {
        objectResult.setExportPaths(new ArrayList<>());
        List<URL> actualExportPaths = objectResult.getExportPaths();
        assertNotNull(actualExportPaths);
        assertEquals(0, actualExportPaths.size());
    }

    // Edge case tests

    // Test null schema in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullSchema() {
        new ObjectResult(null, "name", "type");
    }

    // Test null name in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() {
        new ObjectResult("schema", null, "type");
    }

    // Test null type in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullType() {
        new ObjectResult("schema", "name", null);
    }

    // Test null getters and setters
    @Test(expected = NullPointerException.class)
    public void testNullGettersAndSetters() {
        ObjectResult result = new ObjectResult();
        result.setSchema(null);
    }

    // Test null exportPaths list in setter
    @Test(expected = NullPointerException.class)
    public void testNullExportPathsList() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(null);
    }

    // Test empty constructor with null getters and setters
    @Test
    public void testEmptyConstructorNullGettersAndSetters() {
        ObjectResult result = new ObjectResult();
        result.setSchema(null);
        result.setName(null);
        result.setType(null);
        result.setStatus(null);
        result.setCount(null);
        result.setTotal(null);
        assertNull(result.getSchema());
        assertNull(result.getName());
        assertNull(result.getType());
        assertNull(result.getStatus());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    // Test getters and setters with null values
    @Test
    public void testGettersAndSettersWithNullValues() {
        ObjectResult result = new ObjectResult();
        result.setSchema(null);
        result.setName("name");
        result.setType(null);
        result.setStatus(ObjectStatus.STATUS_OK);
        result.setCount(new AtomicLong(10));
        result.setTotal(new AtomicLong(20));
        assertNull(result.getSchema());
        assertEquals("name", result.getName());
        assertNull(result.getType());
        assertEquals(ObjectStatus.STATUS_OK, result.getStatus());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    // Test getters and setters with empty strings
    @Test
    public void testGettersAndSettersWithEmptyStrings() {
        ObjectResult result = new ObjectResult();
        result.setSchema("");
        result.setName("name");
        result.setType("");
        result.setStatus(ObjectStatus.STATUS_OK);
        result.setCount(new AtomicLong(10));
        result.setTotal(new AtomicLong(20));
        assertEquals("", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("", result.getType());
        assertEquals(ObjectStatus.STATUS_OK, result.getStatus());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    // Test getters and setters with empty lists
    @Test
    public void testGettersAndSettersWithEmptyLists() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(new ArrayList<>());
        List<URL> actualExportPaths = result.getExportPaths();
        assertNotNull(actualExportPaths);
        assertEquals(0, actualExportPaths.size());
    }

    // Test getters and setters with multiple URLs in list
    @Test
    public void testGettersAndSettersWithMultipleURLs() {
        List<URL> exportPaths = Arrays.asList(new URL("path1"), new URL("path2"), new URL("path3"));
        ObjectResult result = new ObjectResult();
        result.setExportPaths(exportPaths);
        List<URL> actualExportPaths = result.getExportPaths();
        assertEquals(exportPaths, actualExportPaths);
        assertEquals(3, actualExportPaths.size());
        assertEquals("path1", actualExportPaths.get(0).getFile());
        assertEquals("path2", actualExportPaths.get(1).getFile());
        assertEquals("path3", actualExportPaths.get(2).getFile());
    }

    // Test exception handling

    // Test IllegalArgumentException when null schema is passed to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentException() {
        new ObjectResult(null, "name", "type");
    }

    // Test IllegalArgumentException when null name is passed to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentExceptionName() {
        new ObjectResult("schema", null, "type");
    }

    // Test IllegalArgumentException when null type is passed to constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentExceptionType() {
        new ObjectResult("schema", "name", null);
    }

    // Test NullPointerException when null getters and setters are used
    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionGettersAndSetters() {
        ObjectResult result = new ObjectResult();
        result.setSchema(null);
    }

    // Test NullPointerException when null exportPaths list is used in setter
    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionExportPathsList() {
        ObjectResult result = new ObjectResult();
        result.setExportPaths(null);
    }

}