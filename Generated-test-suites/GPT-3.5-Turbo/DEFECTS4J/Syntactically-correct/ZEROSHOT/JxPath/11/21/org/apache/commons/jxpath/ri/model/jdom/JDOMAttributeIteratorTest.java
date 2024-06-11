import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.*;

public class JDOMAttributeIteratorTest {
  private JDOMAttributeIterator jdomAttributeIterator;

  @Test
  public void testGetNodePointer() {
    NodePointer parent = new NodePointer();
    QName name = new QName("test", "test");
    jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    NodePointer result = jdomAttributeIterator.getNodePointer();
    Assert.assertNull(result);
  }

  @Test
  public void testGetPosition() {
    NodePointer parent = new NodePointer();
    QName name = new QName("test", "test");
    jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    int result = jdomAttributeIterator.getPosition();
    Assert.assertEquals(0, result);
  }

  @Test
  public void testSetPosition() {
    NodePointer parent = new NodePointer();
    QName name = new QName("test", "test");
    jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    boolean result = jdomAttributeIterator.setPosition(1);
    Assert.assertFalse(result);
  }
}