import org.junit.Assert;
import org.junit.Test;

public class ValueInstantiatorTest {

    @Test
    public void testGetValueClass() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(String.class);
        Assert.assertEquals(String.class, instantiator.getValueClass());
    }

    @Test
    public void testGetValueTypeDesc() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Integer.class);
        Assert.assertEquals("java.lang.Integer", instantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() {
        ValueInstantiator instantiator = new ValueInstantiator.Base(Boolean.class) {
            @Override
            public boolean canCreateUsingDefault() { return true; }
        };
        Assert.assertTrue(instantiator.canInstantiate());
    }

    // TODO: Add more test cases to cover all methods in the class

}