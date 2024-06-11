package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {
  
  @Test
  public void testGetTargetType() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("Type", probe.getTargetType());
  }
  
  @Test
  public void testGetBmName() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("Name", probe.getBmName());
  }
  
  @Test
  public void testGetProbeName() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("Probe", probe.getProbeName());
  }
  
  @Test
  public void testGetTargetName() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("TestTarget", probe.getTargetName());
  }
  
  @Test
  public void testGetBmClass() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("Class", probe.getBmClass());
  }
  
  @Test
  public void testGetBmDescription() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("Description", probe.getBmDescription());
  }
  
  @Test
  public void testGetPrecision() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals(5, probe.getPrecision());
  }
  
  @Test
  public void testGetMeasureURI() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("measureURI", probe.getMeasureURI());
  }
  
  @Test
  public void testGetXmlRpcUrl() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("url", probe.getXmlRpcUrl());
  }
  
  @Test
  public void testToString() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals("ProbeInformation{targetName='TestTarget', targetType='Type', bmClass='Class', bmName='Name', bmDescription='Description', probeDescription='Probe', precision=5}", probe.toString());
  }
  
  @Test
  public void testEquals() {
    ProbeInformation probe1 = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    ProbeInformation probe2 = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    assertEquals(true, probe1.equals(probe2));
  }
  
  @Test
  public void testHashCode() {
    ProbeInformation probe = new ProbeInformation("TestTarget", "Type", "Class", "Name", "Description", "Probe", 5, "url");
    int hashCode = probe.getTargetName() != null ? probe.getTargetName().hashCode() : 0;
    hashCode = 31 * hashCode + (probe.getTargetType() != null ? probe.getTargetType().hashCode() : 0);
    hashCode = 31 * hashCode + (probe.getBmClass() != null ? probe.getBmClass().hashCode() : 0);
    hashCode = 31 * hashCode + (probe.getBmName() != null ? probe.getBmName().hashCode() : 0);
    hashCode = 31 * hashCode + (probe.getBmDescription() != null ? probe.getBmDescription().hashCode() : 0);
    hashCode = 31 * hashCode + (probe.getProbeName() != null ? probe.getProbeName().hashCode() : 0);
    hashCode = 31 * hashCode + probe.getPrecision();
    assertEquals(hashCode, probe.hashCode());
  }
    
}