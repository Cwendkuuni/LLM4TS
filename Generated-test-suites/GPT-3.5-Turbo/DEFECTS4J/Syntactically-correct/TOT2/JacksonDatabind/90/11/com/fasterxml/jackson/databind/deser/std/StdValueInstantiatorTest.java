import static org.junit.Assert.*;
import org.junit.Test;

public class StdValueInstantiatorTest {

    @Test
    public void testConstructor() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertEquals("String", instantiator.getValueTypeDesc());
        assertEquals(String.class, instantiator.getValueClass());
    }

    @Test
    public void testConfigureFromObjectSettings() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getDelegateCreator());
    }

    @Test
    public void testCreateUsingDefault() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createUsingDefault(null));
    }

    @Test
    public void testCreateFromObjectWith() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromObjectWith(null, null));
    }

    @Test
    public void testCreateUsingDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createUsingDelegate(null, null));
    }

    @Test
    public void testCreateUsingArrayDelegate() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createUsingArrayDelegate(null, null));
    }

    @Test
    public void testCreateFromString() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromString(null, ""));
    }

    @Test
    public void testCreateFromInt() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromInt(null, 0));
    }

    @Test
    public void testCreateFromLong() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromLong(null, 0L));
    }

    @Test
    public void testCreateFromDouble() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromDouble(null, 0.0));
    }

    @Test
    public void testCreateFromBoolean() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.createFromBoolean(null, true));
    }

    @Test
    public void testGetters() {
        StdValueInstantiator instantiator = new StdValueInstantiator(null, String.class);
        assertNull(instantiator.getDelegateCreator());
        assertNull(instantiator.getArrayDelegateCreator());
        assertNull(instantiator.getDefaultCreator());
        assertNull(instantiator.getWithArgsCreator());
        assertNull(instantiator.getIncompleteParameter());
    }
}