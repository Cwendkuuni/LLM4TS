import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertNotNull(checkGlobalThis);
  }
}