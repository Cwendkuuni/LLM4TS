import static org.junit.Assert.assertNotNull;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    assertNotNull(checker);
  }
}