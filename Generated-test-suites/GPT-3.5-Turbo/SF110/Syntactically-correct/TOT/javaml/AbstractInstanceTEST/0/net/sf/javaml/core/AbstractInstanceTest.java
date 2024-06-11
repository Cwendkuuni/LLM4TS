import org.junit.Assert;
import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance();
        Assert.assertEquals(0, instance.getID());
    }
    
    @Test
    public void testIterator() {
        AbstractInstance instance = new ConcreteInstance();
        Assert.assertTrue(instance.iterator().hasNext());
        Assert.assertEquals(0.0, instance.iterator().next(), 0.001);
    }
    
    @Test
    public void testClassValue() {
        AbstractInstance instance = new ConcreteInstance();
        Assert.assertNull(instance.classValue());
        instance.setClassValue("classA");
        Assert.assertEquals("classA", instance.classValue());
    }
    
    @Test
    public void testMinus_WhenInstancesHaveEqualAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance min = new ConcreteInstance();
        Instance result = instance.minus(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testMinus_WhenInstancesHaveDifferentAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance min = new ConcreteInstance();
        ((ConcreteInstance) min).put(0, 1.0);
        Instance result = instance.minus(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        Assert.assertEquals(-1.0, result.value(0), 0.001);
        for (int i = 1; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testMinus_WhenValueIsGiven() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 1.0;
        Instance result = instance.minus(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(-1.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testDivide_WhenValueIsZero() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 0.0;
        Instance result = instance.divide(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(Double.POSITIVE_INFINITY, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testDivide_WhenValueIsNotZero() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 2.0;
        Instance result = instance.divide(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testMultiply() {
        AbstractInstance instance = new ConcreteInstance();
        double value = 2.0;
        Instance result = instance.multiply(value);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testHashCode() {
        AbstractInstance instance = new ConcreteInstance();
        Assert.assertEquals(0, instance.hashCode());
    }
    
    @Test
    public void testEquals_WhenSameInstance() {
        AbstractInstance instance = new ConcreteInstance();
        Assert.assertEquals(true, instance.equals(instance));
    }
    
    @Test
    public void testEquals_WhenDifferentInstance() {
        AbstractInstance instance1 = new ConcreteInstance();
        AbstractInstance instance2 = new ConcreteInstance();
        Assert.assertEquals(false, instance1.equals(instance2));
    }
    
    @Test
    public void testMultiply_WhenInstancesHaveEqualAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance other = new ConcreteInstance();
        Instance result = instance.multiply(other);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testMultiply_WhenInstancesHaveDifferentAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance other = new ConcreteInstance();
        ((ConcreteInstance) other).put(0, 2.0);
        Instance result = instance.multiply(other);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        Assert.assertEquals(0.0, result.value(0), 0.001);
        for (int i = 1; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testDivide_WhenInstancesHaveEqualAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance min = new ConcreteInstance();
        Instance result = instance.divide(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(Double.POSITIVE_INFINITY, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testDivide_WhenInstancesHaveDifferentAttributes() {
        AbstractInstance instance = new ConcreteInstance();
        Instance min = new ConcreteInstance();
        ((ConcreteInstance) min).put(0, 2.0);
        Instance result = instance.divide(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.value(0), 0.001);
        for (int i = 1; i < instance.noAttributes(); i++) {
            Assert.assertEquals(Double.NaN, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testAdd_WhenValueIsGiven() {
        AbstractInstance instance = new ConcreteInstance();
        double min = 1.0;
        Instance result = instance.add(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(1.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testAdd_WhenValueIsInstance() {
        AbstractInstance instance = new ConcreteInstance();
        Instance min = new ConcreteInstance();
        Instance result = instance.add(min);
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    @Test
    public void testSqrt() {
        AbstractInstance instance = new ConcreteInstance();
        Instance result = instance.sqrt();
        Assert.assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); i++) {
            Assert.assertEquals(0.0, result.value(i), 0.001);
        }
    }
    
    private static class ConcreteInstance extends AbstractInstance {
        private final Map<Integer, Double> values;
        
        public ConcreteInstance() {
            this.values = new HashMap<>();
            for (int i = 0; i < this.noAttributes(); i++) {
                this.values.put(i, 0.0);
            }
        }
        
        @Override
        public int noAttributes() {
            return 5;
        }
        
        @Override
        public double value(int index) {
            return this.values.get(index);
        }
        
        @Override
        public void put(int index, double value) {
            this.values.put(index, value);
        }
    }
}