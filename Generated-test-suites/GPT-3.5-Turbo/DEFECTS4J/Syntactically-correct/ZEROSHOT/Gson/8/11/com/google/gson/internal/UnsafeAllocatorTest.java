import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private UnsafeAllocator unsafeAllocator;

    @Before
    public void setUp() {
        unsafeAllocator = new UnsafeAllocator() {
            @Override
            public <T> T newInstance(Class<T> c) throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testCreate() {
        UnsafeAllocator allocator = UnsafeAllocator.create();
        assertNotNull(allocator);
    }

    @Test
    public void testNewInstance() {
        Class<String> stringClass = String.class;
        try {
            String instance = unsafeAllocator.newInstance(stringClass);
            assertNull(instance);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedNewInstance() {
        Class<Integer> integerClass = Integer.class;
        unsafeAllocator.newInstance(integerClass);
    }

}