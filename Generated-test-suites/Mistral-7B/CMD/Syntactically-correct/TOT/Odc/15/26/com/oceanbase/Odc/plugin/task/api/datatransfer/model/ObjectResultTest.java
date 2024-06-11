import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObjectResultTest {

    @Test
    public void testObjectResultCreation() {
        // Given
        String schema = "test_schema";
        String name = "test_name";
        String type = "test_type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        // When & Then
        Assertions.assertEquals(schema, objectResult.getSchema());
        Assertions.assertEquals(name, objectResult.getName());
        Assertions.assertEquals(type, objectResult.getType());
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertEquals(ObjectStatus.Status. Success, objectResult.getStatus());
        Assertions.assertEquals(0L, objectResult.getCount().get());
        Assertions.assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultOf() {
        // Given
        ObjectStatus objectStatus = Mockito.mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("test_type");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status. Success);
        when(objectStatus.getName()).thenReturn("test_name");
        when(objectStatus.getSchema()).thenReturn("test_schema");
        when(objectStatus.getCount()).thenReturn(1L);
        when(objectStatus.getTotal()).thenReturn(1L);

        // When
        ObjectResult objectResult = ObjectResult.of(objectStatus);

        // Then
        Assertions.assertEquals("test_schema", objectResult.getSchema());
        Assertions.assertEquals("test_name", objectResult.getName());
        Assertions.assertEquals("test_type", objectResult.getType());
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertEquals(ObjectStatus.Status. Success, objectResult.getStatus());
        Assertions.assertEquals(1L, objectResult.getCount().get());
        Assertions.assertEquals(1L, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        // Given
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("test_schema");
        objectResult.setName("test_name");
        objectResult.setType("test_type");

        // When
        String result = objectResult.getSummary();

        // Then
        Assertions.assertEquals("test_schema.test_name[test_type]", result);
    }

    @Test
    public void testWithInvalidType() {
        // Given
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("test_schema");
        objectResult.setName("test_name");
        objectResult.setType(null);

        // Then
        Assertions.assertThrows(NullPointerException.class, () -> objectResult.getType());
    }

    @Test
    public void testWithEmptyExportPaths() {
        // Given
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("test_schema");
        objectResult.setName("test_name");
        objectResult.setType("test_type");

        // When
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);

        // Then
        Assertions.assertEquals(exportPaths, objectResult.getExportPaths());
        Assertions.assertTrue(objectResult.getExportPaths().isEmpty());
    }

    @Test
    public void testWithNonEmptyExportPaths() {
        // Given
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("test_schema");
        objectResult.setName("test_name");
        objectResult.setType("test_type");

        // When
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://test.com"));
        objectResult.setExportPaths(exportPaths);

        // Then
        Assertions.assertEquals(exportPaths, objectResult.getExportPaths());
        Assertions.assertFalse(objectResult.getExportPaths().isEmpty());
    }
}