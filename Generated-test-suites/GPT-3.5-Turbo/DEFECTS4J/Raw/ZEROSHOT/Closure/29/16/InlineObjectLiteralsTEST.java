import org.junit.*;
import com.google.javascript.jscomp.*;
import com.google.common.base.*;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "1";
      }
    };
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = new Node();
    Node root = new Node();
    inlineObjectLiterals.process(externs, root);

    // Add assertions here
  }
}