
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class PosixParserTest {

@Test
public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    Assert.assertFalse(parser.eatTheRest);
    Assert.assertEquals(0, parser.tokens.size());
}

@Test
public void testFlatten() {
    Options options = new Options();
    PosixParser parser = new PosixParser();
    String[] arguments = {"--foo", "bar", "-abc"};
    String[] flattened = parser.flatten(options, arguments, false);
    Assert.assertEquals(5, flattened.length);
    Assert.assertEquals("--foo", flattened[0]);
    Assert.assertEquals("bar", flattened[1]);
    Assert.assertEquals("-", flattened[2]);
    Assert.assertEquals("-a", flattened[3]);
    Assert.assertEquals("-bc", flattened[4]);
}

@Test
public void testGobble() {
    PosixParser parser = new PosixParser();
    parser.eatTheRest = true;
    parser.gobble(Arrays.asList("--foo", "bar").iterator());
    List tokens = parser.tokens;
    Assert.assertEquals("--foo", tokens.get(0));
    Assert.assertEquals("bar", tokens.get(1));
}

@Test
public void testProcessNonOptionToken() {
    PosixParser parser = new PosixParser();
    parser.processNonOptionToken("value");
    List tokens = parser.tokens;
    Assert.assertEquals("--", tokens.get(0));
    Assert.assertEquals("value", tokens.get(1));
}

@Test
public void testProcessOptionToken() {
    Options options = new Options();
    options.addOption("f", false, "option f");
    PosixParser parser = new PosixParser();
    parser.options = options;
    parser.processOptionToken("-f", false);
    List tokens = parser.tokens;
    Assert.assertEquals("-f", tokens.get(0));
}

@Test
public void testBurstToken() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", false, "option b");
    PosixParser parser = new PosixParser();
    parser.options = options;
    parser.burstToken("-ab", true);
    List tokens = parser.tokens;
    Assert.assertEquals("-a", tokens.get(0));
    Assert.assertEquals("-b", tokens.get(1));
}

}
