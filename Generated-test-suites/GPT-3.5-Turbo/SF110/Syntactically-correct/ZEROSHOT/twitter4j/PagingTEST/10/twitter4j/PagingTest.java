package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

  @Test
  public void testConstructorWithoutArguments() {
    Paging paging = new Paging();
    assertEquals(-1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithPageArgument() {
    Paging paging = new Paging(1);
    assertEquals(1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithSinceIdArgument() {
    Paging paging = new Paging(1000L);
    assertEquals(-1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(1000L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithPageAndCountArguments() {
    Paging paging = new Paging(1, 10);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(-1L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithPageAndSinceIdArguments() {
    Paging paging = new Paging(1, 1000L);
    assertEquals(1, paging.getPage());
    assertEquals(-1, paging.getCount());
    assertEquals(1000L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithPageCountSinceIdArguments() {
    Paging paging = new Paging(1, 10, 1000L);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(1000L, paging.getSinceId());
    assertEquals(-1L, paging.getMaxId());
  }

  @Test
  public void testConstructorWithPageCountSinceIdMaxIdArguments() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    assertEquals(1, paging.getPage());
    assertEquals(10, paging.getCount());
    assertEquals(1000L, paging.getSinceId());
    assertEquals(2000L, paging.getMaxId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetPageWithNegativePageValue() {
    Paging paging = new Paging();
    paging.setPage(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetCountWithNegativeCountValue() {
    Paging paging = new Paging();
    paging.setCount(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetSinceIdWithNegativeSinceIdValue() {
    Paging paging = new Paging();
    paging.setSinceId(-1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxIdWithNegativeMaxIdValue() {
    Paging paging = new Paging();
    paging.setMaxId(-1L);
  }

  @Test
  public void testCountMethod() {
    Paging paging = new Paging();
    assertSame(paging, paging.count(10));
    assertEquals(10, paging.getCount());
  }

  @Test
  public void testSinceIdMethod() {
    Paging paging = new Paging();
    assertSame(paging, paging.sinceId(1000L));
    assertEquals(1000L, paging.getSinceId());
  }

  @Test
  public void testMaxIdMethod() {
    Paging paging = new Paging();
    assertSame(paging, paging.maxId(2000L));
    assertEquals(2000L, paging.getMaxId());
  }

  @Test
  public void testEqualsMethod() {
    Paging paging1 = new Paging(1, 10, 1000L, 2000L);
    Paging paging2 = new Paging(1, 10, 1000L, 2000L);
    Paging paging3 = new Paging(2, 10, 1000L, 2000L);
    Paging paging4 = new Paging(1, 20, 1000L, 2000L);
    Paging paging5 = new Paging(1, 10, 2000L, 2000L);
    Paging paging6 = new Paging(1, 10, 1000L, 3000L);

    assertTrue(paging1.equals(paging2));
    assertFalse(paging1.equals(paging3));
    assertFalse(paging1.equals(paging4));
    assertFalse(paging1.equals(paging5));
    assertFalse(paging1.equals(paging6));
  }

  @Test
  public void testHashCodeMethod() {
    Paging paging1 = new Paging(1, 10, 1000L, 2000L);
    Paging paging2 = new Paging(1, 10, 1000L, 2000L);

    assertEquals(paging1.hashCode(), paging2.hashCode());
  }

  @Test
  public void testToStringMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);

    assertEquals("Paging{page=1, count=10, sinceId=1000, maxId=2000}", paging.toString());
  }

  @Test
  public void testAsPostParameterListMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    List<HttpParameter> parameters = paging.asPostParameterList();

    assertEquals(4, parameters.size());
    assertEquals("since_id", parameters.get(0).getName());
    assertEquals("1000", parameters.get(0).getValue());
    assertEquals("max_id", parameters.get(1).getName());
    assertEquals("2000", parameters.get(1).getValue());
    assertEquals("count", parameters.get(2).getName());
    assertEquals("10", parameters.get(2).getValue());
    assertEquals("page", parameters.get(3).getName());
    assertEquals("1", parameters.get(3).getValue());
  }

  @Test
  public void testAsPostParameterArrayMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    HttpParameter[] parameters = paging.asPostParameterArray();

    assertEquals(4, parameters.length);
    assertEquals("since_id", parameters[0].getName());
    assertEquals("1000", parameters[0].getValue());
    assertEquals("max_id", parameters[1].getName());
    assertEquals("2000", parameters[1].getValue());
    assertEquals("count", parameters[2].getName());
    assertEquals("10", parameters[2].getValue());
    assertEquals("page", parameters[3].getName());
    assertEquals("1", parameters[3].getValue());
  }

  @Test
  public void testAsPostParameterListWithSupportedParamsMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    char[] supportedParams = { 's', 'm', 'c', 'p' };
    List<HttpParameter> parameters = paging.asPostParameterList(supportedParams);

    assertEquals(4, parameters.size());
    assertEquals("since_id", parameters.get(0).getName());
    assertEquals("1000", parameters.get(0).getValue());
    assertEquals("max_id", parameters.get(1).getName());
    assertEquals("2000", parameters.get(1).getValue());
    assertEquals("count", parameters.get(2).getName());
    assertEquals("10", parameters.get(2).getValue());
    assertEquals("page", parameters.get(3).getName());
    assertEquals("1", parameters.get(3).getValue());
  }

  @Test
  public void testAsPostParameterArrayWithSupportedParamsMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    char[] supportedParams = { 's', 'm', 'c', 'p' };
    HttpParameter[] parameters = paging.asPostParameterArray(supportedParams);

    assertEquals(4, parameters.length);
    assertEquals("since_id", parameters[0].getName());
    assertEquals("1000", parameters[0].getValue());
    assertEquals("max_id", parameters[1].getName());
    assertEquals("2000", parameters[1].getValue());
    assertEquals("count", parameters[2].getName());
    assertEquals("10", parameters[2].getValue());
    assertEquals("page", parameters[3].getName());
    assertEquals("1", parameters[3].getValue());
  }

  @Test
  public void testAsPostParameterListWithSupportedParamsAndPerPageParamNameMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    char[] supportedParams = { 's', 'm', 'c', 'p' };
    String perPageParamName = "per_page";
    List<HttpParameter> parameters = paging.asPostParameterList(supportedParams, perPageParamName);

    assertEquals(4, parameters.size());
    assertEquals("since_id", parameters.get(0).getName());
    assertEquals("1000", parameters.get(0).getValue());
    assertEquals("max_id", parameters.get(1).getName());
    assertEquals("2000", parameters.get(1).getValue());
    assertEquals("per_page", parameters.get(2).getName());
    assertEquals("10", parameters.get(2).getValue());
    assertEquals("page", parameters.get(3).getName());
    assertEquals("1", parameters.get(3).getValue());
  }

  @Test
  public void testAsPostParameterArrayWithSupportedParamsAndPerPageParamNameMethod() {
    Paging paging = new Paging(1, 10, 1000L, 2000L);
    char[] supportedParams = { 's', 'm', 'c', 'p' };
    String perPageParamName = "per_page";
    HttpParameter[] parameters = paging.asPostParameterArray(supportedParams, perPageParamName);

    assertEquals(4, parameters.length);
    assertEquals("since_id", parameters[0].getName());
    assertEquals("1000", parameters[0].getValue());
    assertEquals("max_id", parameters[1].getName());
    assertEquals("2000", parameters[1].getValue());
    assertEquals("per_page", parameters[2].getName());
    assertEquals("10", parameters[2].getValue());
    assertEquals("page", parameters[3].getName());
    assertEquals("1", parameters[3].getValue());
  }
}