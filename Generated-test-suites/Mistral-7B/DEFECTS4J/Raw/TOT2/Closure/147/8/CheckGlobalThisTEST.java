import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertInstanceOf(CheckGlobalThis.class, checkGlobalThis);
  }
}