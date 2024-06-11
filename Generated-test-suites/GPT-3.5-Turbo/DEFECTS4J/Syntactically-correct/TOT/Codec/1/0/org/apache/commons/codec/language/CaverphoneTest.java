import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

    @Test
    public void testCaverphone_TypicalUseCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("typicalusecase");
        Assert.assertEquals("52p5kal1a1", result);
    }

    @Test
    public void testCaverphone_EdgeCase_EmptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_ErrorScenario_NullInput() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        Assert.assertEquals("1111111111", result);
    }

    @Test
    public void testEncode_TypicalUseCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("typicalusecase");
        Assert.assertEquals("52p5kal1a1", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_ErrorScenario_InvalidInputType() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode(123);
    }

    @Test
    public void testIsCaverphoneEqual_TypicalUseCase_StringsEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("typicalusecase", "typicalusecase");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_TypicalUseCase_StringsNotEqual() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("typicalusecase", "differentstring");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_TypicalUseCase_EqualCaverphones() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("typicalusecase", "typ1c1lu5ec1se");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_TypicalUseCase_NotEqualCaverphones() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("typicalusecase", "d1ff5r2ntstr1ng");
        Assert.assertFalse(result);
    }

}