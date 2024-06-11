import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneConstructor() {
 // Test default constructor
 }

 @Test
 public void testCaverphoneNullInput() {
 String input = null;
 String expected = "1111111111";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String input = "";
 String expected = "1111111111";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "HelloWorld";
 String expected = "H2oW2rld";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabet() {
 String input = "123abc456Def!";
 String expected = "abcDef";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "cat";
 String expected = "cat";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 String inputWithE = "catE";
 String expectedWithE = "cat";
 String outputWithE = caverphone.caverphone(inputWithE);
 assertEquals(expectedWithE, outputWithE);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "cough";
 String expected = "cou2f";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 String inputRough = "rough";
 String expectedRough = "rou2f";
 String outputRough = caverphone.caverphone(inputRough);
 assertEquals(expectedRough, outputRough);
 String inputTough = "tough";
 String expectedTough = "tou2f";
 String outputTough = caverphone.caverphone(inputTough);
 assertEquals(expectedTough, outputTough);
 String inputEnough = "enough";
 String expectedEnough = "enou2f";
 String outputEnough = caverphone.caverphone(inputEnough);
 assertEquals(expectedEnough, outputEnough);
 String inputTrough = "trough";
 String expectedTrough = "trou2f";
 String outputTrough = caverphone.caverphone(inputTrough);
 assertEquals(expectedTrough, outputTrough);
 }

 @Test
 public void testCaverphoneReplace() {
 String input = "cqiicecytch";
 String expected = "2qisis2ch";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 String inputCe = "ce";
 String expectedCe = "se";
 String outputCe = caverphone.caverphone(inputCe);
 assertEquals(expectedCe, outputCe);
 String inputCy = "cy";
 String expectedCy = "sy";
 String outputCy = caverphone.caverphone(inputCy);
 assertEquals(expectedCy, outputCy);
 String inputTch = "tch";
 String expectedTch = "2ch";
 String outputTch = caverphone.caverphone(inputTch);
 assertEquals(expectedTch, outputTch);
 String inputC = "c";
 String expectedC = "k";
 String outputC = caverphone.caverphone(inputC);
 assertEquals(expectedC, outputC);
 String inputQ = "q";
 String expectedQ = "k";
 String outputQ = caverphone.caverphone(inputQ);
 assertEquals(expectedQ, outputQ);
 String inputX = "x";
 String expectedX = "k";
 String outputX = caverphone.caverphone(inputX);
 assertEquals(expectedX, outputX);
 String inputV = "v";
 String expectedV = "f";
 String outputV = caverphone.caverphone(inputV);
 assertEquals(expectedV, outputV);
 String inputDg = "dg";
 String expectedDg = "2g";
 String outputDg = caverphone.caverphone(inputDg);
 assertEquals(expectedDg, outputDg);
 String inputTio = "tio";
 String expectedTio = "sio";
 String outputTio = caverphone.caverphone(inputTio);
 assertEquals(expectedTio, outputTio);
 String inputTia = "tia";
 String expectedTia = "sia";
 String outputTia = caverphone.caverphone(inputTia);
 assertEquals(expectedTia, outputTia);
 String inputD = "d";
 String expectedD = "t";
 String outputD = caverphone.caverphone(inputD);
 assertEquals(expectedD, outputD);
 String inputPh = "ph";
 String expectedPh = "fh";
 String outputPh = caverphone.caverphone(inputPh);
 assertEquals(expectedPh, outputPh);
 String inputB = "b";
 String expectedB = "p";
 String outputB = caverphone.caverphone(inputB);
 assertEquals(expectedB, outputB);
 String inputSh = "sh";
 String expectedSh = "s2";
 String outputSh = caverphone.caverphone(inputSh);
 assertEquals(expectedSh, outputSh);
 String inputZ = "z";
 String expectedZ = "s";
 String outputZ = caverphone.caverphone(inputZ);
 assertEquals(expectedZ, outputZ);
 String inputAe = "aeiou";
 String expectedAe = "A";
 String outputAe = caverphone.caverphone(inputAe);
 assertNull(outputAe);
 String inputI = "i";
 String expectedI = "3";
 String outputI = caverphone.caverphone(inputI);
 assertEquals(expectedI, outputI);
 String inputJ = "j";
 String expectedJ = "y";
 String outputJ = caverphone.caverphone(inputJ);
 assertEquals(expectedJ, outputJ);
 String inputY3 = "y3";
 String expectedY3 = "Y3";
 String outputY3 = caverphone.caverphone(inputY3);
 assertEquals(expectedY3, outputY3);
 String inputGh3 = "gh3";
 String expectedGh3 = "3kh3";
 String outputGh3 = caverphone.caverphone(inputGh3);
 assertEquals(expectedGh3, outputGh3);
 String inputGh = "gh";
 String expectedGh = "22";
 String outputGh = caverphone.caverphone(inputGh);
 assertEquals(expectedGh, outputGh);
 String inputG = "g";
 String expectedG = "k";
 String outputG = caverphone.caverphone(inputG);
 assertEquals(expectedG, outputG);
 String inputS = "s+";
 String expectedS = "S";
 String outputS = caverphone.caverphone(inputS);
 assertEquals(expectedS, outputS);
 String inputT = "t+";
 String expectedT = "T";
 String outputT = caverphone.caverphone(inputT);
 assertEquals(expectedT, outputT);
 String inputP = "p+";
 String expectedP = "P";
 String outputP = caverphone.caverphone(inputP);
 assertEquals(expectedP, outputP);
 String inputK = "k+";
 String expectedK = "K";
 String outputK = caverphone.caverphone(inputK);
 assertEquals(expectedK, outputK);
 String inputF = "f+";
 String expectedF = "F";
 String outputF = caverphone.caverphone(inputF);
 assertEquals(expectedF, outputF);
 String inputM = "m+";
 String expectedM = "M";
 String outputM = caverphone.caverphone(inputM);
 assertEquals(expectedM, outputM);
 String inputN = "n+";
 String expectedN = "N";
 String outputN = caverphone.caverphone(inputN);
 assertEquals(expectedN, outputN);
 String inputW3 = "w3";
 String expectedW3 = "W3";
 String outputW3 = caverphone.caverphone(inputW3);
 assertEquals(expectedW3, outputW3);
 //String inputWhy = "why";
 //String expectedWhy = "Why";
 //String outputWhy = caverphone.caverphone(inputWhy);
 //assertEquals(expectedWhy, outputWhy);
 String inputWh3 = "wh3";
 String expectedWh3 = "Wh3";
 String outputWh3 = caverphone.caverphone(inputWh3);
 assertEquals(expectedWh3, outputWh3);
 String inputW = "w";
 String expectedW = "2";
 String outputW = caverphone.caverphone(inputW);
 assertEquals(expectedW, outputW);
 String inputH = "h";
 String expectedH = "A";
 String outputH = caverphone.caverphone(inputH);
 assertEquals(expectedH, outputH);
 String inputR3 = "r3";
 String expectedR3 = "R3";
 String outputR3 = caverphone.caverphone(inputR3);
 assertEquals(expectedR3, outputR3);
 String inputR = "r";
 String expectedR = "2";
 String outputR = caverphone.caverphone(inputR);
 assertEquals(expectedR, outputR);
 String inputL3 = "l3";
 String expectedL3 = "L3";
 String outputL3 = caverphone.caverphone(inputL3);
 assertEquals(expectedL3, outputL3);
 String inputL = "l";
 String expectedL = "2";
 String outputL = caverphone.caverphone(inputL);
 assertEquals(expectedL, outputL);
 //String inputLy = "ly";
 //String expectedLy = "Ly";
 //String outputLy = caverphone.caverphone(inputLy);
 //assertEquals(expectedLy, outputLy);
 String inputJy = "jy";
 String expectedJy = "y";
 String outputJy = caverphone.caverphone(inputJy);
 assertEquals(expectedJy, outputJy);
 }

 @Test
 public void testCaverphoneRemovals() {
 String input = "2";
 String expected = "";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 String inputA = "A";
 String expectedA = "";
 String outputA = caverphone.caverphone(inputA);
 assertEquals(expectedA, outputA);
 String input = "3$";
 String expected3 = "A";
 String output3 = caverphone.caverphone(input);
 assertEquals(expected3, output3);
 String expectedEmpty = "";
 String outputEmpty = caverphone.caverphone(expectedEmpty);
 assertEquals(expectedEmpty, outputEmpty);
 }

 @Test
 public void testCaverphonePutTenOnEnd() {
 String input = "abc";
 String expected = "abc1111111111";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneTakeFirstSix() {
 String input = "abcdefghijkl";
 String expected = "abcdef";
 String output = caverphone.caverphone(input).substring(0, 10);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneEncodeObject() {
 Object input = "HelloWorld";
 String expected = "H2oW2rld";
 String output = (String) caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneEncodeString() {
 String input = "HelloWorld";
 String expected = "H2oW2rld";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String input1 = "HelloWorld";
 String input2 = "HelloWorld";
 boolean expected = true;
 boolean output = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, output);
 String input1Prime = "HelloWorld";
 String input2Prime = "HelloWorldPrime";
 boolean expectedPrime = false;
 boolean outputPrime = caverphone.isCaverphoneEqual(input1Prime, input2Prime);
 assertEquals(expectedPrime, outputPrime);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneEncodeNonString() {
 Object input = 123;
 caverphone.encode(input);
 }

}