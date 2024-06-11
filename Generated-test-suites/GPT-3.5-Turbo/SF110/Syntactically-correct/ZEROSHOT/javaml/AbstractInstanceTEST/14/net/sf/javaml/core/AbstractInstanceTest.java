import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {
   
   @Test
   public void testGetID() {
      AbstractInstance instance = new AbstractInstance() {
      };
      int id = instance.getID();
      assertEquals(0, id);
   }

   @Test
   public void testIterator() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Iterator<Double> iterator = instance.iterator();
      assertNotNull(iterator);
   }

   @Test
   public void testClassValue() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Object classValue = instance.classValue();
      assertNull(classValue);
   }

   @Test
   public void testSetClassValue() {
      AbstractInstance instance = new AbstractInstance() {
      };
      instance.setClassValue("Test");
      Object classValue = instance.classValue();
      assertEquals("Test", classValue);
   }

   @Test
   public void testMinus() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      Instance out = instance.minus(min);
      assertNotNull(out);
   }

   @Test
   public void testMinusDouble() {
      AbstractInstance instance = new AbstractInstance() {
      };
      double min = 0.0;
      Instance out = instance.minus(min);
      assertNotNull(out);
   }

   @Test
   public void testDivide() {
      AbstractInstance instance = new AbstractInstance() {
      };
      double min = 0.0;
      Instance out = instance.divide(min);
      assertNotNull(out);
   }

   @Test
   public void testMultiply() {
      AbstractInstance instance = new AbstractInstance() {
      };
      double value = 0.0;
      Instance out = instance.multiply(value);
      assertNotNull(out);
   }

   @Test
   public void testHashCode() {
      AbstractInstance instance = new AbstractInstance() {
      };
      int hashCode = instance.hashCode();
      assertEquals(0, hashCode);
   }

   @Test
   public void testEquals() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Object obj = new AbstractInstance() {
      };
      boolean equals = instance.equals(obj);
      assertFalse(equals);
   }

   @Test
   public void testMultiplyInstance() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Instance value = new DenseInstance(new double[instance.noAttributes()]);
      Instance out = instance.multiply(value);
      assertNotNull(out);
   }

   @Test
   public void testDivideInstance() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      Instance out = instance.divide(min);
      assertNotNull(out);
   }

   @Test
   public void testAddDouble() {
      AbstractInstance instance = new AbstractInstance() {
      };
      double min = 0.0;
      Instance out = instance.add(min);
      assertNotNull(out);
   }

   @Test
   public void testAddInstance() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Instance min = new DenseInstance(new double[instance.noAttributes()]);
      Instance out = instance.add(min);
      assertNotNull(out);
   }

   @Test
   public void testSqrt() {
      AbstractInstance instance = new AbstractInstance() {
      };
      Instance out = instance.sqrt();
      assertNotNull(out);
   }
}