import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

  private NodePointer parentMock;
  private QName nameMock;
  private DOMAttributeIterator iterator;

  @Before
  public void setup() {
    parentMock = mock(NodePointer.class);
    nameMock = mock(QName.class);
    iterator = new DOMAttributeIterator(parentMock, nameMock);
  }

  @Test
  public void testConstructor() {
    assertNotNull(iterator);
  }

  @Test
  public void testGetNodePointer() {
    DOMAttributePointer attributePointer = mock(DOMAttributePointer.class);
    when(parentMock.getNode()).thenReturn(mock(Node.class));
    when(iterator.getNodePointer()).thenReturn(attributePointer);

    NodePointer result = iterator.getNodePointer();

    verify(parentMock, times(1)).getNamespaceURI(anyString());
    verify(parentMock, times(1)).getNamespaceResolver();
    verify(parentMock, times(1)).getNode();
    assertNotNull(result);
    assertEquals(attributePointer, result);
  }

  @Test
  public void testGetPosition() {
    int result = iterator.getPosition();

    assertEquals(0, result);
  }

  @Test
  public void testSetPositionWithValidPosition() {
    boolean result = iterator.setPosition(1);

    assertTrue(result);
    assertEquals(1, iterator.getPosition());
  }

  @Test
  public void testSetPositionWithInvalidPosition() {
    boolean result = iterator.setPosition(-1);

    assertFalse(result);
    assertEquals(-1, iterator.getPosition());
  }

  @Test
  public void testSetPositionWithValidPositionGreaterThanSize() {
    int attributesSize = 5;
    when(parentMock.getNode()).thenReturn(mock(Node.class));
    when(iterator.getNodePointer()).thenReturn(mock(DOMAttributePointer.class));
    iterator.setPosition(attributesSize);

    boolean result = iterator.setPosition(attributesSize + 1);

    assertFalse(result);
    assertEquals(attributesSize, iterator.getPosition());
  }

}