import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AbstractInstanceTest {
  
  @Test
  public void testGetID() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    int id = instance.getID();
    assertEquals(0, id);
  }
  
  @Test
  public void testIterator() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    Iterator<Double> iterator = instance.iterator();
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testClassValue() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    Object classValue = instance.classValue();
    assertNull(classValue);
  }
  
  @Test
  public void testSetClassValue() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    instance.setClassValue("Test");
    assertEquals("Test", instance.classValue());
  }
  
  @Test
  public void testMinusInstance() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance min = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 5;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance out = instance.minus(min);
    double value = out.value(0);
    assertEquals(5, value, 0);
  }
  
  @Test
  public void testMinusDouble() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    double min = 5;
    
    Instance out = instance.minus(min);
    double value = out.value(0);
    assertEquals(5, value, 0);
  }
  
  @Test
  public void testDivide() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    double min = 5;
    
    Instance out = instance.divide(min);
    double value = out.value(0);
    assertEquals(2, value, 0);
  }
  
  @Test
  public void testMultiply() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    double value = 2;
    
    Instance out = instance.multiply(value);
    double result = out.value(0);
    assertEquals(20, result, 0);
  }
  
  @Test
  public void testHashCode() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    int hashCode = instance.hashCode();
    assertEquals(0, hashCode);
  }
  
  @Test
  public void testEquals() {
    AbstractInstance instance1 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    AbstractInstance instance2 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 0;
      }
    };
    
    boolean equals = instance1.equals(instance2);
    assertTrue(equals);
  }
  
  @Test
  public void testMultiplyInstance() {
    AbstractInstance instance1 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    AbstractInstance instance2 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 5;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance out = instance1.multiply(instance2);
    double value = out.value(0);
    assertEquals(50, value, 0);
  }
  
  @Test
  public void testDivideInstance() {
    AbstractInstance instance1 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    AbstractInstance instance2 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 5;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance out = instance1.divide(instance2);
    double value = out.value(0);
    assertEquals(2, value, 0);
  }
  
  @Test
  public void testAddDouble() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    double min = 5;
    
    Instance out = instance.add(min);
    double value = out.value(0);
    assertEquals(15, value, 0);
  }
  
  @Test
  public void testAddInstance() {
    AbstractInstance instance1 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 10;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    AbstractInstance instance2 = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 5;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance out = instance1.add(instance2);
    double value = out.value(0);
    assertEquals(15, value, 0);
  }
  
  @Test
  public void testSqrt() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 4;
      }
      
      @Override
      public int noAttributes() {
        return 1;
      }
    };
    
    Instance out = instance.sqrt();
    double value = out.value(0);
    assertEquals(2, value, 0);
  }
  
  @Test
  public void testInstanceValueIterator() {
    AbstractInstance instance = new AbstractInstance() {
      @Override
      public double value(int index) {
        return 0;
      }
      
      @Override
      public int noAttributes() {
        return 3;
      }
    };
    
    Iterator<Double> iterator = instance.new InstanceValueIterator();
    assertTrue(iterator.hasNext());
    assertEquals(0, iterator.next().doubleValue(), 0);
    assertTrue(iterator.hasNext());
    assertEquals(0, iterator.next().doubleValue(), 0);
    assertTrue(iterator.hasNext());
    assertEquals(0, iterator.next().doubleValue(), 0);
    assertFalse(iterator.hasNext());
  }
}