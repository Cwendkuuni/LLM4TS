
import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest {

    ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Test
    public void testExcludeField() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        assertFalse(reflectiveTypeAdapterFactory.excludeField(null, true));
        assertFalse(reflectiveTypeAdapterFactory.excludeField(null, false));
    }

    @Test
    public void testGetFieldNames() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        assertTrue(reflectiveTypeAdapterFactory.getFieldNames(null).isEmpty());
    }

    @Test
    public void testCreate() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        assertNull(reflectiveTypeAdapterFactory.create(null, null));
    }

    @Test
    public void testCreateBoundField() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        assertNull(reflectiveTypeAdapterFactory.createBoundField(null, null, null, null, false, false));
    }

    @Test
    public void testGetBoundFields() {
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(null, null, null);
        assertTrue(reflectiveTypeAdapterFactory.getBoundFields(null, null, null).isEmpty());
    }

    @Test
    public void testAdapterRead() {
        ReflectiveTypeAdapterFactory.Adapter<?> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        assertNull(adapter.read(null));
    }

    @Test
    public void testAdapterWrite() {
        ReflectiveTypeAdapterFactory.Adapter<?> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        adapter.write(null, null);
    }

    @Test
    public void testAdapterWrite_withNonNullValue() {
        ReflectiveTypeAdapterFactory.Adapter<?> adapter = new ReflectiveTypeAdapterFactory.Adapter<>(null, null);
        adapter.write(null, new Object());
    }

}
