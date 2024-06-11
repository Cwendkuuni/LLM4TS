import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("fr", lang.guessLanguage("bonjour"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("de", lang.guessLanguage("hallo"));
        assertEquals("ru", lang.guessLanguage("привет"));
        assertEquals("ja", lang.guessLanguage("こんにちは"));
        assertEquals("ko", lang.guessLanguage("안녕하세요"));
        assertEquals("zh", lang.guessLanguage("你好"));
        assertEquals("ar", lang.guessLanguage("مرحبا"));
        assertEquals("__UNKNOWN__", lang.guessLanguage("abc"));

        lang = Lang.instance(NameType.ASIAN_NAME);
        assertEquals("zh", lang.guessLanguage("张三"));
        assertEquals("ko", lang.guessLanguage("김철수"));
        assertEquals("ja", lang.guessLanguage("山田太郎"));

        lang = Lang.instance(NameType.SOUTHEAST_ASIAN);
        assertEquals("vi", lang.guessLanguage("Nguyen"));
        assertEquals("th", lang.guessLanguage("Sakda"));
        assertEquals("ms", lang.guessLanguage("Mohd"));

        lang = Lang.instance(NameType.INDIAN);
        assertEquals("hi", lang.guessLanguage("Rajesh"));
        assertEquals("bn", lang.guessLanguage("Siddhartha"));
        assertEquals("te", lang.guessLanguage("Naveen"));
    }
}