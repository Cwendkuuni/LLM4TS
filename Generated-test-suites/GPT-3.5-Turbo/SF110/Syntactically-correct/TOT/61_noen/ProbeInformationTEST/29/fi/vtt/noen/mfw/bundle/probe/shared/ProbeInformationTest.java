import org.junit.Assert;
import org.junit.Test;

public class ProbeInformationTest{

    //Test cases for getTargetType() method
    @Test
    public void testGetTargetType(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Type1", pi.getTargetType());
    }

    //Test cases for getBmName() method
    @Test
    public void testGetBmName(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Name1", pi.getBmName());
    }

    //Test cases for getProbeName() method
    @Test
    public void testGetProbeName(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Probe1", pi.getProbeName());
    }

    //Test cases for getTargetName() method
    @Test
    public void testGetTargetName(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Target1", pi.getTargetName());
    }

    //Test cases for getBmClass() method
    @Test
    public void testGetBmClass(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Class1", pi.getBmClass());
    }

    //Test cases for getBmDescription() method
    @Test
    public void testGetBmDescription(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("Description1", pi.getBmDescription());
    }

    //Test cases for getPrecision() method
    @Test
    public void testGetPrecision(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals(5, pi.getPrecision());
    }

    //Test cases for getXmlRpcUrl() method
    @Test
    public void testGetXmlRpcUrl(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        Assert.assertEquals("url1", pi.getXmlRpcUrl());
    }

    //Test cases for getMeasureURI() method
    @Test
    public void testGetMeasureURI(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        String measureURI = "Type1_Target1_Class1_Name1";
        Assert.assertEquals(measureURI, pi.getMeasureURI());
    }

    //Test cases for toString() method
    @Test
    public void testToString(){
        ProbeInformation pi = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        String expectedString = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='Class1', bmName='Name1', bmDescription='Description1', probeDescription='Probe1', precision=5}";
        Assert.assertEquals(expectedString, pi.toString());
    }

    //Test cases for equals() method
    @Test
    public void testEquals(){
        ProbeInformation pi1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        ProbeInformation pi2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        ProbeInformation pi3 = new ProbeInformation("Target2", "Type2", "Class2", "Name2", "Description2", "Probe2", 10, "url2");

        Assert.assertTrue(pi1.equals(pi2));
        Assert.assertFalse(pi1.equals(pi3));
    }

    //Test cases for hashCode() method
    @Test
    public void testHashCode(){
        ProbeInformation pi1 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");
        ProbeInformation pi2 = new ProbeInformation("Target1", "Type1", "Class1", "Name1", "Description1", "Probe1", 5, "url1");

        Assert.assertEquals(pi1.hashCode(), pi2.hashCode());
    }

}