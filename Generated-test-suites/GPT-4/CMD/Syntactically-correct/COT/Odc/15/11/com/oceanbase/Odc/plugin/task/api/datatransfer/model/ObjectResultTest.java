package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.assertEquals;
    import static org.junit.Assert.assertTrue;
    import static org.junit.Assert.assertNull;
    import org.junit.Test;
    import java.net.URL;
    import java.util.concurrent.atomic.AtomicLong;

    public class ObjectResultTest {

        @Test
        public void testConstructor() {
            ObjectResult objectResult = new ObjectResult("schema", "name", "type");
            assertEquals("schema", objectResult.getSchema());
            assertEquals("name", objectResult.getName());
            assertEquals("type", objectResult.getType());
            assertTrue(objectResult.getCount() instanceof AtomicLong);
            assertTrue(objectResult.getTotal() instanceof AtomicLong);
        }

        @Test
        public void testOf() {
            ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
            objectStatus.setType("type");
            objectStatus.setSchema("schema");
            ObjectResult objectResult = ObjectResult.of(objectStatus);
            assertEquals("schema", objectResult.getSchema());
            assertEquals("name", objectResult.getName());
            assertEquals("type", objectResult.getType());
            assertEquals(0, objectResult.getCount().get());
            assertEquals(0, objectResult.getTotal().get());
        }

        @Test
        public void testGetSummary() {
            ObjectResult objectResult = new ObjectResult("schema", "name", "type");
            String summary = objectResult.getSummary();
            assertEquals("schema.name[type]", summary);
        }

        @Test
        public void testConstructorWithNulls() {
            ObjectResult objectResult = new ObjectResult(null, null, null);
            assertNull(objectResult.getSchema());
            assertNull(objectResult.getName());
            assertNull(objectResult.getType());
        }

        @Test(expected = NullPointerException.class)
        public void testOfWithNull() {
            ObjectResult.of(null);
        }
        
    }