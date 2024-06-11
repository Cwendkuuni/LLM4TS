
package jaw.entrada;

import jaw.metamodelo.ProcessarEntidades;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SalvarTest {
  
  private ProcessarEntidades form;
  private Salvar salvar;
  
  @Before
  public void setUp() {
    form = mock(ProcessarEntidades.class);
    salvar = new Salvar(form, Salvar.SALVAR);
  }
  
  @Test
  public void testSalvarConstructor() {
    assertNotNull(salvar);
    assertEquals(false, salvar.vela);
    assertEquals("", salvar.arquivo);
    assertEquals("", salvar.xml);
    assertNotNull(salvar.entidade);
  }
  
  @Test
  public void testSalvarWithTipoSalvar10() {
    Salvar salvarTipo10 = new Salvar(form, Salvar.SALVAR_XML);
    
    assertNotNull(salvarTipo10);
    assertEquals(false, salvarTipo10.vela);
    assertEquals("", salvarTipo10.arquivo);
    assertEquals("", salvarTipo10.xml);
    assertNotNull(salvarTipo10.entidade);
  }
  
  @Test
  public void testSalvarWithTipoSalvar30() {
    Salvar salvarTipo30 = new Salvar(form, Salvar.SALVAR_COMO);
    
    assertNotNull(salvarTipo30);
    assertEquals(false, salvarTipo30.vela);
    assertEquals("", salvarTipo30.arquivo);
    assertEquals("", salvarTipo30.xml);
    assertNotNull(salvarTipo30.entidade);
  }
  
  @Test
  public void testSalvarWithTipoSalvar20() {
    when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    when(escolhedorMock.getSelectedFile()).thenReturn(null);
    when(form.getName()).thenReturn("");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
  
  @Test
  public void testSalvarWithNoSelectedFileAndFormNameNotBlank() {
    when(form.getNomeArquivoSalvo()).thenReturn("");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    when(escolhedorMock.getSelectedFile()).thenReturn(null);
    when(form.getName()).thenReturn("Form Name");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
  
  @Test
  public void testSalvarWithSelectedFileAndFormName() {
    when(form.getNomeArquivoSalvo()).thenReturn("Saved File");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    File selectedFileMock = mock(File.class);
    when(selectedFileMock.getAbsolutePath()).thenReturn("/path/to/file");
    when(escolhedorMock.getSelectedFile()).thenReturn(selectedFileMock);
    when(form.getName()).thenReturn("Form Name");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("/path/to/file", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
  
  @Test
  public void testSalvarWithSelectedFileAndVelaFalse() {
    Salvar salvarVelaFalse = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarVelaFalse);
    assertEquals(true, salvarVelaFalse.vela);
    assertEquals("", salvarVelaFalse.arquivo);
    assertEquals("", salvarVelaFalse.xml);
    assertNotNull(salvarVelaFalse.entidade);
  }
  
  @Test
  public void testSalvarWithSavedFileNotEndsWithJawAndVelaTrue() {
    when(form.getNomeArquivoSalvo()).thenReturn("Saved File");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    File selectedFileMock = mock(File.class);
    when(selectedFileMock.getAbsolutePath()).thenReturn("/path/to/file");
    when(escolhedorMock.getSelectedFile()).thenReturn(selectedFileMock);
    when(form.getName()).thenReturn("New Project");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("/path/to/file.jaw", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
  
  @Test
  public void testSalvarWithSavedFileEndsWithJawAndVelaTrue() {
    when(form.getNomeArquivoSalvo()).thenReturn("Saved File.jaw");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    File selectedFileMock = mock(File.class);
    when(selectedFileMock.getAbsolutePath()).thenReturn("/path/to/file");
    when(escolhedorMock.getSelectedFile()).thenReturn(selectedFileMock);
    when(form.getName()).thenReturn("New Project");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("/path/to/file.jaw", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
  
  @Test
  public void testSalvarWithTipoSalvar20AndVelaFalse() {
    Salvar salvarTipo20VelaFalse = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20VelaFalse);
    assertEquals(true, salvarTipo20VelaFalse.vela);
    assertEquals("", salvarTipo20VelaFalse.arquivo);
    assertEquals("", salvarTipo20VelaFalse.xml);
    assertNotNull(salvarTipo20VelaFalse.entidade);
  }
  
  @Test
  public void testSalvarWithTipoSalvar20AndFormNameEqualsToNewProject() {
    when(form.getNomeArquivoSalvo()).thenReturn("Saved File");
    JFileChooser escolhedorMock = mock(JFileChooser.class);
    File selectedFileMock = mock(File.class);
    when(selectedFileMock.getAbsolutePath()).thenReturn("/path/to/file");
    when(escolhedorMock.getSelectedFile()).thenReturn(selectedFileMock);
    when(form.getName()).thenReturn("New Project");
    
    JFileChooser escolhedorSpy = spy(escolhedorMock);
    
    Salvar salvarTipo20 = new Salvar(form, Salvar.SALVAR);
    
    assertNotNull(salvarTipo20);
    assertEquals(true, salvarTipo20.vela);
    assertEquals("/path/to/file.jaw", salvarTipo20.arquivo);
    assertEquals("", salvarTipo20.xml);
    assertNotNull(salvarTipo20.entidade);
  }
}

