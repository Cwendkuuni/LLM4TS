import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

   @Test
   public void testCompare() {
       // Typical use case
       AttributeModel model1 = new AttributeModel("apple");
       AttributeModel model2 = new AttributeModel("banana");
       AttributeModelComparator comparator = new AttributeModelComparator();
       
       int result = comparator.compare(model1, model2);
       assertTrue(result < 0);
       
       // Edge case where names are equal
       model1 = new AttributeModel("apple");
       model2 = new AttributeModel("Apple");
       
       result = comparator.compare(model1, model2);
       assertEquals(0, result);
       
       // Edge case where one name is "a" and the other is not
       model1 = new AttributeModel("a");
       model2 = new AttributeModel("b");
       
       result = comparator.compare(model1, model2);
       assertEquals(1, result);
       
       // Exception handling scenario with null inputs
       model1 = null;
       model2 = new AttributeModel("b");
       
       result = comparator.compare(model1, model2);
       assertEquals(-1, result);
   }
}