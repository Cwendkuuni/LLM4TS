import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

   @Test
   public void testGetCurrentNodePointer() {
       // given
       EvalContext parentContext = new EvalContext();
       NodeTest nodeTest = new NodeTest();

       // when
       AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
       NodePointer result = attributeContext.getCurrentNodePointer();

       // then
       assertNull(result);
   }

   @Test
   public void testReset() {
       // given
       EvalContext parentContext = new EvalContext();
       NodeTest nodeTest = new NodeTest();

       // when
       AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
       attributeContext.reset();

       // then
       assertFalse(attributeContext.setStarted);
       assertNull(attributeContext.iterator);
       assertNull(attributeContext.getCurrentNodePointer());
   }

   @Test
   public void testSetPosition() {
       // given
       EvalContext parentContext = new EvalContext();
       NodeTest nodeTest = new NodeTest();

       // when
       AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
       boolean result = attributeContext.setPosition(5);

       // then
       assertFalse(result);
   }

   @Test
   public void testNextNode() {
       // given
       EvalContext parentContext = new EvalContext();
       NodeTest nodeTest = new NodeTest();

       // when
       AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
       boolean result = attributeContext.nextNode();

       // then
       assertFalse(result);
       assertNull(attributeContext.currentNodePointer);
   }

}