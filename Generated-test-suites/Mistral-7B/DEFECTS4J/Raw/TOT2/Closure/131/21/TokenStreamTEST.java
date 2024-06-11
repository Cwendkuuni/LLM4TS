import static org.junit.Assert.assertEquals;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    assertEquals(true, TokenStream.isKeyword("if"));
    assertEquals(true, TokenStream.isKeyword("else"));
    assertEquals(true, TokenStream.isKeyword("switch"));
    assertEquals(true, TokenStream.isKeyword("case"));
    assertEquals(true, TokenStream.isKeyword("class"));
    assertEquals(true, TokenStream.isKeyword("var"));
    // Add more test cases as needed
  }
}