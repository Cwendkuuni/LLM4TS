import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult();
        Assertions.assertNotNull(objectResult);
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertNull(objectResult.getSchema());
        Assertions.assertNull(objectResult.getName());
        Assertions.assertNull(objectResult.getType());
        Assertions.assertEquals(ObjectStatus.Status.CREATED, objectResult.getStatus());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getCount());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getTotal());

        ObjectResult objectResultWithSchemaNameType = new ObjectResult("schema1", "name1", "type1");
        Assertions.assertNotNull(objectResultWithSchemaNameType);
        Assertions.assertNull(objectResultWithSchemaNameType.getExportPaths());
        Assertions.assertEquals("schema1", objectResultWithSchemaNameType.getSchema());
        Assertions.assertEquals("name1", objectResultWithSchemaNameType.getName());
        Assertions.assertEquals("type1", objectResultWithSchemaNameType.getType());
        Assertions.assertEquals(ObjectStatus.Status.CREATED, objectResultWithSchemaNameType.getStatus());
        Assertions.assertEquals(new AtomicLong(0), objectResultWithSchemaNameType.getCount());
        Assertions.assertEquals(new AtomicLong(0), objectResultWithSchemaNameType.getTotal());
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.CREATED);
        when(objectStatus.getName()).thenReturn("name1");
        when(objectStatus.getSchema()).thenReturn("schema1");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(5));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        Assertions.assertNotNull(objectResult);
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertEquals("schema1", objectResult.getSchema());
        Assertions.assertEquals("name1", objectResult.getName());
        Assertions.assertEquals("type1", objectResult.getType());
        Assertions.assertEquals(ObjectStatus.Status.CREATED, objectResult.getStatus());
        Assertions.assertEquals(new AtomicLong(5), objectResult.getCount());
        Assertions.assertEquals(new AtomicLong(10), objectResult.getTotal());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        Assertions.assertEquals("null.null[null]", objectResult.getSummary());

        ObjectResult objectResultWithSchemaNameType = new ObjectResult("schema1", "name1", "type1");
        Assertions.assertEquals("schema1.name1[type1]", objectResultWithSchemaNameType.getSummary());
    }
}