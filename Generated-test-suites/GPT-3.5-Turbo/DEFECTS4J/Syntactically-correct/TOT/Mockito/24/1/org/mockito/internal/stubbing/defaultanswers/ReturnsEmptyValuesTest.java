import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

  @Test
  public void testToStringDefaultName() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.toString()).thenAnswer(values);
    assertEquals("Mock for Object, hashCode: " + mock.hashCode(), mock.toString());
  }

  @Test
  public void testToStringCustomName() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class, "CustomMock");
    when(mock.toString()).thenAnswer(values);
    assertEquals("CustomMock", mock.toString());
  }

  @Test
  public void testCompareTo() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);

    when(mock1.compareTo(mock1)).thenAnswer(values);
    assertEquals(0, mock1.compareTo(mock1));
    when(mock1.compareTo(mock2)).thenAnswer(values);
    assertNotEquals(0, mock1.compareTo(mock2));
  }

  @Test
  public void testPrimitiveReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithPrimitiveReturn()).thenAnswer(values);
    assertEquals(0, mock.getMethodWithPrimitiveReturn());
  }

  @Test
  public void testWrapperClassReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithWrapperClassReturn()).thenAnswer(values);
    assertEquals(0, mock.getMethodWithWrapperClassReturn());
  }

  @Test
  public void testCollectionReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithCollectionReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithCollectionReturn().isEmpty());
  }

  @Test
  public void testSetReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithSetReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithSetReturn().isEmpty());
  }

  @Test
  public void testHashSetReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithHashSetReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithHashSetReturn().isEmpty());
  }

  @Test
  public void testSortedSetReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithSortedSetReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithSortedSetReturn().isEmpty());
  }

  @Test
  public void testTreeSetReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithTreeSetReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithTreeSetReturn().isEmpty());
  }

  @Test
  public void testLinkedHashSetReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithLinkedHashSetReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithLinkedHashSetReturn().isEmpty());
  }

  @Test
  public void testListReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithListReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithListReturn().isEmpty());
  }

  @Test
  public void testLinkedListReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithLinkedListReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithLinkedListReturn().isEmpty());
  }

  @Test
  public void testArrayListReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithArrayListReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithArrayListReturn().isEmpty());
  }

  @Test
  public void testMapReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithMapReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithMapReturn().isEmpty());
  }

  @Test
  public void testHashMapReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithHashMapReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithHashMapReturn().isEmpty());
  }

  @Test
  public void testSortedMapReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithSortedMapReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithSortedMapReturn().isEmpty());
  }

  @Test
  public void testTreeMapReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithTreeMapReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithTreeMapReturn().isEmpty());
  }

  @Test
  public void testLinkedHashMapReturn() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    Object mock = mock(Object.class);
    when(mock.getMethodWithLinkedHashMapReturn()).thenAnswer(values);
    assertTrue(mock.getMethodWithLinkedHashMapReturn().isEmpty());
  }

  // Additional test cases can be added for other methods if needed

}