import org.junit.Test;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void process_withInlineObjectLiterals() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "safe_name_id";
      }
    };
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node externs = IR.root();
    Node root = IR.root();

    // Add code to test

    inlineObjectLiterals.process(externs, root);

    // Assert code changes
  }
}