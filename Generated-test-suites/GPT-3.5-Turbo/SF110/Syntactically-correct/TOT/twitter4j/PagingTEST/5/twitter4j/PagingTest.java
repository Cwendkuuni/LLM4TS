import static org.junit.Assert.*;
import org.junit.Test;

public class PagingTest {

@Test
public void testConstructor1() {
    Paging paging = new Paging();
    assertEquals(-1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor2() {
    Paging paging = new Paging(1);
    assertEquals(1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor3() {
    Paging paging = new Paging(10);
    assertEquals(10, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor4() {
    Paging paging = new Paging(1, 10);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor5() {
    Paging paging = new Paging(10, 20);
    assertEquals(10, paging.getPage());
    assertEquals(20, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor6() {
    Paging paging = new Paging(1, 10, 100);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(100L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor7() {
    Paging paging = new Paging(10, 20, 200);
    assertEquals(10, paging.getPage());
    assertEquals(20, paging.getCount());
    assertEquals(200L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
}

@Test
public void testConstructor8() {
    Paging paging = new Paging(1, 10, 100, 1000);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(100L, paging.getSinceId());
    assertEquals(1000L, paging.getMaxId());
}

@Test
public void testConstructor9() {
    Paging paging = new Paging(10, 20, 200, 2000);
    assertEquals(10, paging.getPage());
    assertEquals(20, paging.getCount());
    assertEquals(200L, paging.getSinceId());
    assertEquals(2000L, paging.getMaxId());
}

@Test(expected = IllegalArgumentException.class)
public void testSetPageWithNegativeValue() {
    Paging paging = new Paging();
    paging.setPage(-1);
}

@Test
public void testSetPageWithPositiveValue() {
    Paging paging = new Paging();
    paging.setPage(1);
    assertEquals(1, paging.getPage());
}

@Test(expected = IllegalArgumentException.class)
public void testSetCountWithNegativeValue() {
    Paging paging = new Paging();
    paging.setCount(-1);
}

@Test
public void testSetCountWithPositiveValue() {
    Paging paging = new Paging();
    paging.setCount(10);
    assertEquals(10, paging.getCount());
}

@Test(expected = IllegalArgumentException.class)
public void testSetSinceIdWithNegativeValue() {
    Paging paging = new Paging();
    paging.setSinceId(-1);
}

@Test
public void testSetSinceIdWithPositiveValue() {
    Paging paging = new Paging();
    paging.setSinceId(100);
    assertEquals(100, paging.getSinceId());
}

@Test(expected = IllegalArgumentException.class)
public void testSetMaxIdWithNegativeValue() {
    Paging paging = new Paging();
    paging.setMaxId(-1);
}

@Test
public void testSetMaxIdWithPositiveValue() {
    Paging paging = new Paging();
    paging.setMaxId(1000);
    assertEquals(1000, paging.getMaxId());
}

@Test
public void testEquals() {
    Paging paging1 = new Paging(1, 10, 100, 1000);
    Paging paging2 = new Paging(1, 10, 100, 1000);
    assertTrue(paging1.equals(paging2));
}

@Test
public void testNotEquals() {
    Paging paging1 = new Paging(1, 10, 100, 1000);
    Paging paging2 = new Paging(2, 20, 200, 2000);
    assertFalse(paging1.equals(paging2));
}

@Test
public void testHashCode() {
    Paging paging = new Paging(1, 10, 100, 1000);
    int expectedHashCode = 31 * (31 * (31 * 1 + 10) + (int)(100 ^ (100 >>> 32))) + (int)(1000 ^ (1000 >>> 32));
    assertEquals(expectedHashCode, paging.hashCode());
}

@Test
public void testToString() {
    Paging paging = new Paging(1, 10, 100, 1000);
    String expectedString = "Paging{page=1, count=10, sinceId=100, maxId=1000}";
    assertEquals(expectedString, paging.toString());
}

}