import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {

    AbstractInstance instance;

    @Before
    public void setUp() {
        instance = new ConcreteInstance();
    }

    @Test
    public void testGetID() {
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        assertNotNull(instance.iterator());
    }

    @Test
    public void testClassValue() {
        assertNull(instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        Object value = "test";
        instance.setClassValue(value);
        assertEquals(value, instance.classValue());
    }

    @Test
    public void testMinusInstance() {
        Instance other = new ConcreteInstance();
        Instance result = instance.minus(other);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testMinusDouble() {
        double value = 5.0;
        Instance result = instance.minus(value);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testDivide() {
        double value = 2.0;
        Instance result = instance.divide(value);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testMultiply() {
        double value = 2.0;
        Instance result = instance.multiply(value);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, instance.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));

        Object differentClass = new Object();
        assertFalse(instance.equals(differentClass));

        AbstractInstance otherInstance = new ConcreteInstance();
        otherInstance.setClassValue("test");
        assertFalse(instance.equals(otherInstance));

        otherInstance.setClassValue(null);
        otherInstance.ID = 1;
        assertFalse(instance.equals(otherInstance));
    }

    @Test
    public void testMultiplyInstance() {
        Instance other = new ConcreteInstance();
        Instance result = instance.multiply(other);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testDivideInstance() {
        Instance other = new ConcreteInstance();
        Instance result = instance.divide(other);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testAddDouble() {
        double value = 5.0;
        Instance result = instance.add(value);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testAddInstance() {
        Instance other = new ConcreteInstance();
        Instance result = instance.add(other);
        assertEquals(0, result.noAttributes());
    }

    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertEquals(0, result.noAttributes());
    }

}