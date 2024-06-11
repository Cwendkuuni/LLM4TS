import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

  @Test
  public void testCaverphoneEmptyString() {
    Caverphone caverphone = new Caverphone();
    assertEquals("1111111111", caverphone.caverphone(""));
  }

  @Test
  public void testCaverphoneLowerCase() {
    Caverphone caverphone = new Caverphone();
    assertEquals("71111111C", caverphone.caverphone("caverphone"));
  }

  @Test
  public void testCaverphoneRemoveNonAlpha() {
    Caverphone caverphone = new Caverphone();
    assertEquals("C186", caverphone.caverphone("Hello, World!"));
  }

  @Test
  public void testCaverphoneStartOptions() {
    Caverphone caverphone = new Caverphone();
    assertEquals("cou2f71", caverphone.caverphone("cough"));
    assertEquals("rou2f71", caverphone.caverphone("rough"));
    assertEquals("tou2f71", caverphone.caverphone("tough"));
    assertEquals("enou2f71", caverphone.caverphone("enough"));
    assertEquals("trou2f71", caverphone.caverphone("trough"));
    assertEquals("2n71", caverphone.caverphone("gn"));
    assertEquals("m271", caverphone.caverphone("mb"));
  }

  @Test
  public void testCaverphoneReplacements() {
    Caverphone caverphone = new Caverphone();
    assertEquals("2q3", caverphone.caverphone("cq"));
    assertEquals("si3", caverphone.caverphone("ci"));
    assertEquals("se3", caverphone.caverphone("ce"));
    assertEquals("sy3", caverphone.caverphone("cy"));
    assertEquals("2ch3", caverphone.caverphone("tch"));
    assertEquals("k", caverphone.caverphone("c"));
    assertEquals("k", caverphone.caverphone("q"));
    assertEquals("k", caverphone.caverphone("x"));
    assertEquals("f", caverphone.caverphone("v"));
    assertEquals("2g", caverphone.caverphone("dg"));
    assertEquals("sio", caverphone.caverphone("tio"));
    assertEquals("sia", caverphone.caverphone("tia"));
    assertEquals("t", caverphone.caverphone("d"));
    assertEquals("fh", caverphone.caverphone("ph"));
    assertEquals("p", caverphone.caverphone("b"));
    assertEquals("s2", caverphone.caverphone("sh"));
    assertEquals("s", caverphone.caverphone("z"));
    assertEquals("A", caverphone.caverphone("a"));
    assertEquals("3", caverphone.caverphone("e"));
    assertEquals("3", caverphone.caverphone("i"));
    assertEquals("3", caverphone.caverphone("o"));
    assertEquals("3", caverphone.caverphone("u"));
    assertEquals("y", caverphone.caverphone("j"));
    assertEquals("Y3", caverphone.caverphone("y3"));
    assertEquals("A", caverphone.caverphone("y"));
    assertEquals("3", caverphone.caverphone("y"));
    assertEquals("3kh3", caverphone.caverphone("3gh3"));
    assertEquals("22", caverphone.caverphone("gh"));
    assertEquals("k", caverphone.caverphone("g"));
    assertEquals("S", caverphone.caverphone("s"));
    assertEquals("T", caverphone.caverphone("t"));
    assertEquals("P", caverphone.caverphone("p"));
    assertEquals("K", caverphone.caverphone("k"));
    assertEquals("F", caverphone.caverphone("f"));
    assertEquals("M", caverphone.caverphone("m"));
    assertEquals("N", caverphone.caverphone("n"));
    assertEquals("W3", caverphone.caverphone("w3"));
    assertEquals("Wh3", caverphone.caverphone("wh3"));
    assertEquals("3", caverphone.caverphone("w"));
    assertEquals("A", caverphone.caverphone("h"));
    assertEquals("2", caverphone.caverphone("r"));
    assertEquals("3", caverphone.caverphone("r"));
    assertEquals("2", caverphone.caverphone("l"));
    assertEquals("3", caverphone.caverphone("l"));
  }

  @Test
  public void testCaverphoneRemovals() {
    Caverphone caverphone = new Caverphone();
    assertEquals("", caverphone.caverphone("2"));
    assertEquals("A", caverphone.caverphone("3"));
    assertEquals("", caverphone.caverphone("3"));
  }

  @Test
  public void testEncodeString() {
    Caverphone caverphone = new Caverphone();
    assertEquals("71", caverphone.encode("caverphone"));
  }

  @Test
  public void testEncodeObject() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    assertEquals("71", caverphone.encode((Object) "caverphone"));
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    assertTrue(caverphone.isCaverphoneEqual("caverphone", "caverphone"));
    assertFalse(caverphone.isCaverphoneEqual("caverphone", "hello"));
  }

}