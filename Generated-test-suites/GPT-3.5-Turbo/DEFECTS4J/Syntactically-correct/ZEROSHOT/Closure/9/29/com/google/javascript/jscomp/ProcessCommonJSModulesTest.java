import org.junit.Test;

public class ProcessCommonJSModulesTest {
  
  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
    
    String filename1 = "file.js";
    String moduleName1 = pcm.guessCJSModuleName(filename1);
    String expectedModuleName1 = "module$file";
    assertEquals(expectedModuleName1, moduleName1);
    
    String filename2 = "./file.js";
    String moduleName2 = pcm.guessCJSModuleName(filename2);
    String expectedModuleName2 = "module$file";
    assertEquals(expectedModuleName2, moduleName2);
    
    String filename3 = "../file.js";
    String moduleName3 = pcm.guessCJSModuleName(filename3);
    String expectedModuleName3 = "module$file";
    assertEquals(expectedModuleName3, moduleName3);
    
    String filename4 = "dir/file.js";
    String moduleName4 = pcm.guessCJSModuleName(filename4);
    String expectedModuleName4 = "module$dir$file";
    assertEquals(expectedModuleName4, moduleName4);
    
    String filename5 = "dir1/dir2/file.js";
    String moduleName5 = pcm.guessCJSModuleName(filename5);
    String expectedModuleName5 = "module$dir1$dir2$file";
    assertEquals(expectedModuleName5, moduleName5);
  }
  
  @Test
  public void testToModuleName() {
    String filename1 = "file.js";
    String moduleName1 = ProcessCommonJSModules.toModuleName(filename1);
    String expectedModuleName1 = "module$file";
    assertEquals(expectedModuleName1, moduleName1);
    
    String filename2 = "./file.js";
    String moduleName2 = ProcessCommonJSModules.toModuleName(filename2);
    String expectedModuleName2 = "module$file";
    assertEquals(expectedModuleName2, moduleName2);
    
    String filename3 = "../file.js";
    String moduleName3 = ProcessCommonJSModules.toModuleName(filename3);
    String expectedModuleName3 = "module$file";
    assertEquals(expectedModuleName3, moduleName3);
    
    String filename4 = "dir/file.js";
    String moduleName4 = ProcessCommonJSModules.toModuleName(filename4);
    String expectedModuleName4 = "module$dir$file";
    assertEquals(expectedModuleName4, moduleName4);
    
    String filename5 = "dir1/dir2/file.js";
    String moduleName5 = ProcessCommonJSModules.toModuleName(filename5);
    String expectedModuleName5 = "module$dir1$dir2$file";
    assertEquals(expectedModuleName5, moduleName5);
  }
  
  @Test
  public void testToModuleNameWithCurrentFilename() {
    String requiredFilename1 = "./file.js";
    String currentFilename1 = "dir1/file.js";
    String moduleName1 = ProcessCommonJSModules.toModuleName(requiredFilename1, currentFilename1);
    String expectedModuleName1 = "module$file";
    assertEquals(expectedModuleName1, moduleName1);
    
    String requiredFilename2 = "../file.js";
    String currentFilename2 = "dir1/dir2/file.js";
    String moduleName2 = ProcessCommonJSModules.toModuleName(requiredFilename2, currentFilename2);
    String expectedModuleName2 = "module$file";
    assertEquals(expectedModuleName2, moduleName2);
    
    String requiredFilename3 = "../../file.js";
    String currentFilename3 = "dir1/dir2/dir3/file.js";
    String moduleName3 = ProcessCommonJSModules.toModuleName(requiredFilename3, currentFilename3);
    String expectedModuleName3 = "module$file";
    assertEquals(expectedModuleName3, moduleName3);
    
    String requiredFilename4 = "./file.js";
    String currentFilename4 = "dir1/file1.js";
    String moduleName4 = ProcessCommonJSModules.toModuleName(requiredFilename4, currentFilename4);
    String expectedModuleName4 = "module$dir1$file";
    assertEquals(expectedModuleName4, moduleName4);
    
    String requiredFilename5 = "../file.js";
    String currentFilename5 = "dir1/dir2/file1.js";
    String moduleName5 = ProcessCommonJSModules.toModuleName(requiredFilename5, currentFilename5);
    String expectedModuleName5 = "module$dir1$file";
    assertEquals(expectedModuleName5, moduleName5);
    
    String requiredFilename6 = "../../file.js";
    String currentFilename6 = "dir1/dir2/dir3/file1.js";
    String moduleName6 = ProcessCommonJSModules.toModuleName(requiredFilename6, currentFilename6);
    String expectedModuleName6 = "module$dir1$dir2$file";
    assertEquals(expectedModuleName6, moduleName6);
  }
  
}