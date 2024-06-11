import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PagingTest {
  
  @Test
  public void testDefaultConstructor() {
    Paging paging = new Paging();
    assertEquals(-1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testPageConstructor() {
    Paging paging = new Paging(10);
    assertEquals(10, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testSinceIdConstructor() {
    Paging paging = new Paging(100L);
    assertEquals(-1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(100L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testPageCountConstructor() {
    Paging paging = new Paging(5, 20);
    assertEquals(5, paging.getPage());
    assertEquals(20, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testPageSinceIdConstructor() {
    Paging paging = new Paging(3, 50L);
    assertEquals(3, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(50L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testPageCountSinceIdConstructor() {
    Paging paging = new Paging(1, 10, 200L);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(200L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }
  
  @Test
  public void testPageCountSinceIdMaxIdConstructor() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    assertEquals(2, paging.getPage());
    assertEquals(30, paging.getCount());
    assertEquals(300L, paging.getSinceId());
    assertEquals(500L, paging.getMaxId());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetPageWithNegativeValue() {
    Paging paging = new Paging();
    paging.setPage(-1);
  }
  
  @Test
  public void testSetPageWithPositiveValue() {
    Paging paging = new Paging();
    paging.setPage(5);
    assertEquals(5, paging.getPage());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetCountWithNegativeValue() {
    Paging paging = new Paging();
    paging.setCount(-1);
  }
  
  @Test
  public void testSetCountWithPositiveValue() {
    Paging paging = new Paging();
    paging.setCount(20);
    assertEquals(20, paging.getCount());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetSinceIdWithNegativeValue() {
    Paging paging = new Paging();
    paging.setSinceId(-1L);
  }
  
  @Test
  public void testSetSinceIdWithPositiveValue() {
    Paging paging = new Paging();
    paging.setSinceId(100L);
    assertEquals(100L, paging.getSinceId());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxIdWithNegativeValue() {
    Paging paging = new Paging();
    paging.setMaxId(-1L);
  }
  
  @Test
  public void testSetMaxIdWithPositiveValue() {
    Paging paging = new Paging();
    paging.setMaxId(200L);
    assertEquals(200L, paging.getMaxId());
  }
  
  @Test
  public void testEquals() {
    Paging paging1 = new Paging(1, 10, 100L, 200L);
    Paging paging2 = new Paging(1, 10, 100L, 200L);
    assertEquals(paging1, paging2);
  }
  
  @Test
  public void testNotEquals() {
    Paging paging1 = new Paging(1, 10, 100L, 200L);
    Paging paging2 = new Paging(2, 10, 100L, 200L);
    assertNotEquals(paging1, paging2);
  }
  
  @Test
  public void testHashCode() {
    Paging paging1 = new Paging(1, 10, 100L, 200L);
    Paging paging2 = new Paging(1, 10, 100L, 200L);
    assertEquals(paging1.hashCode(), paging2.hashCode());
  }
  
  @Test
  public void testToString() {
    Paging paging = new Paging(1, 10, 100L, 200L);
    String expected = "Paging{page=1, count=10, sinceId=100, maxId=200}";
    assertEquals(expected, paging.toString());
  }
  
  @Test
  public void testAsPostParameterList() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    List<HttpParameter> parameterList = paging.asPostParameterList();
    List<HttpParameter> expected = Arrays.asList(
      new HttpParameter("since_id", "300"),
      new HttpParameter("max_id", "500"),
      new HttpParameter("count", "30"),
      new HttpParameter("page", "2")
    );
    assertEquals(expected, parameterList);
  }
  
  @Test
  public void testAsPostParameterArray() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    HttpParameter[] parameterArray = paging.asPostParameterArray();
    HttpParameter[] expected = new HttpParameter[] {
      new HttpParameter("since_id", "300"),
      new HttpParameter("max_id", "500"),
      new HttpParameter("count", "30"),
      new HttpParameter("page", "2")
    };
    assertArrayEquals(expected, parameterArray);
  }
  
  @Test
  public void testAsPostParameterListWithSupportedParams() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    List<HttpParameter> parameterList = paging.asPostParameterList(Paging.S);
    List<HttpParameter> expected = Arrays.asList(
      new HttpParameter("since_id", "300")
    );
    assertEquals(expected, parameterList);
  }
  
  @Test
  public void testAsPostParameterArrayWithSupportedParams() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    HttpParameter[] parameterArray = paging.asPostParameterArray(Paging.S);
    HttpParameter[] expected = new HttpParameter[] {
      new HttpParameter("since_id", "300")
    };
    assertArrayEquals(expected, parameterArray);
  }
  
  @Test
  public void testAsPostParameterListWithPerPageParamName() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    List<HttpParameter> parameterList = paging.asPostParameterList(Paging.SMCP, "per_page");
    List<HttpParameter> expected = Arrays.asList(
      new HttpParameter("since_id", "300"),
      new HttpParameter("max_id", "500"),
      new HttpParameter("per_page", "30"),
      new HttpParameter("page", "2")
    );
    assertEquals(expected, parameterList);
  }
  
  @Test
  public void testAsPostParameterArrayWithPerPageParamName() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    HttpParameter[] parameterArray = paging.asPostParameterArray(Paging.SMCP, "per_page");
    HttpParameter[] expected = new HttpParameter[] {
      new HttpParameter("since_id", "300"),
      new HttpParameter("max_id", "500"),
      new HttpParameter("per_page", "30"),
      new HttpParameter("page", "2")
    };
    assertArrayEquals(expected, parameterArray);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testAddPostParameterNotSupported() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    paging.addPostParameter(new char[] {'a', 'b'}, 'c', new ArrayList<HttpParameter>(), "count", paging.getCount());
  }
  
  @Test
  public void testAddPostParameterSupported() {
    Paging paging = new Paging(2, 30, 300L, 500L);
    List<HttpParameter> parameterList = new ArrayList<HttpParameter>();
    paging.addPostParameter(Paging.SMCP, 'c', parameterList, "count", paging.getCount());
    List<HttpParameter> expected = Arrays.asList(
      new HttpParameter("count", "30")
    );
    assertEquals(expected, parameterList);
  }
}