import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testProcess() {
    // create a compiler object
    AbstractCompiler compiler = new AbstractCompiler();

    // create a supplier object
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    };

    // create an instance of InlineObjectLiterals
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // create a dummy externs node
    Node externs = new Node(Token.EXTERN_CALL);

    // create a dummy root node
    Node root = new Node(Token.ROOT);

    // call the process method
    inlineObjectLiterals.process(externs, root);

    // Assert your conditions here
    // ...
  }

}