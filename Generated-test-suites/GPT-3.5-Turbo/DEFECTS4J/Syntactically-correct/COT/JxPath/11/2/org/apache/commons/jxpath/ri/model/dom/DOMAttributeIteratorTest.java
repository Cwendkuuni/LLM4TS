
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

  @Mock
  private NodePointer parent;

  private QName name;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    name = new QName("attr");
  }

  @Test
  public void testConstructor() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    assertNotNull("Constructor failed", iterator);
  }

  @Test
  public void testGetNodePointerWithValidPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    iterator.setPosition(1);

    NodePointer nodePointer = iterator.getNodePointer();

    assertNotNull("getNodePointer() returned null", nodePointer);
  }

  @Test
  public void testGetNodePointerWithInvalidPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    iterator.setPosition(0);

    NodePointer nodePointer = iterator.getNodePointer();

    assertEquals("getNodePointer() did not return null for invalid position", null, nodePointer);
  }

  @Test
  public void testGetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    iterator.setPosition(1);

    int position = iterator.getPosition();

    assertEquals("getPosition() returned incorrect position", 1, position);
  }

  @Test
  public void testSetPositionWithValidPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    boolean result = iterator.setPosition(1);

    assertEquals("setPosition() returned incorrect result", true, result);
  }

  @Test
  public void testSetPositionWithInvalidPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    boolean result = iterator.setPosition(0);

    assertEquals("setPosition() returned incorrect result", false, result);
  }
}
