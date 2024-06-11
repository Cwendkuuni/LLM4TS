import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.OK, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        AtomicLong count = new AtomicLong(10);
        AtomicLong total = new AtomicLong(20);
        ObjectStatus objectStatus = new ObjectStatus("name1", "status1", count, total, null);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("name1", objectResult.getName());
        assertEquals("status1", objectResult.getStatus().getName());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
        assertEquals("type1", objectResult.getType());
        assertEquals("schema1", objectResult.getSchema());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSchema() {
        new ObjectResult(null, "name1", "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new ObjectResult("schema1", null, "type1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullType() {
        new ObjectResult("schema1", "name1", null);
    }

    @Test
    public void testSetStatus() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        ObjectStatus status = new ObjectStatus("status1", null, null, null, null);
        objectResult.setStatus(status);
        assertEquals("status1", objectResult.getStatus().getName());
    }

    @Test
    public void testGetExportPathsWhenNotNull() {
        List<URL> exportPaths = Arrays.asList(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1") {
            @JsonIgnore
            public List<URL> getExportPaths() {
                return exportPaths;
            }
        };
        assertEquals(exportPaths, objectResult.getExportPaths());
    }

    @Test
    public void testGetExportPathsWhenNull() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertNull(objectResult.getExportPaths());
    }

}