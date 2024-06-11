package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jaw.ProcessarEntidades;
import jaw.gui.Main;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class AbrirTest {
  
  private Abrir abrir;
  private ProcessarEntidades form;
  private JFileChooser fileChooser;
  
  @Before
  public void setUp() {
    form = mock(ProcessarEntidades.class);
    abrir = new Abrir(form, "sample.xml");
    fileChooser = mock(JFileChooser.class);
  }
  
  @Test
  public void testAbrirConstructorWithArquivoXmlEndsWithJawXml() {
    abrir = new Abrir(form, "sample.jaw.xml");
    verify(form).setNomeArquivoSalvo("sample.jaw.xml");
    verify(form).setEntidades(anyObject());
  }
  
  @Test
  public void testAbrirConstructorWithArquivoNotXmlEndsWithJaw() {
    abrir = new Abrir(form, "sample.jaw");
    verify(JOptionPane).showMessageDialog(anyObject(), anyString(), anyString(), anyInt());
  }
  
  @Test
  public void testAbrirConstructorWithTipoAbrirTen() {
    abrir = new Abrir(form, Abrir.ABRIR_XML);
    verify(fileChooser).setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
    verify(fileChooser).setFileFilter(anyObject());
    verify(fileChooser).showOpenDialog(null);
    verify(form).setEntidades(anyObject());
  }
  
  @Test
  public void testAbrirConstructorWithTipoAbrirTwenty() {
    abrir = new Abrir(form, Abrir.ABRIR);
    verify(fileChooser).setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
    verify(fileChooser).setFileFilter(anyObject());
    verify(fileChooser).showOpenDialog(null);
    verify(form).setNomeArquivoSalvo(anyObject());
    verify(form).setEntidades(anyObject());
  }

  @Test
  public void testAbrirSerializadoWithFileEndsWithJaw() {
    abrir.abrirSerializado("sample.jaw");
    verify(form).setNomeArquivoSalvo("sample.jaw");
    verify(form).setEntidades(anyObject());
  }
  
  @Test
  public void testAbrirSerializadoWithFileNotEndsWithJaw() {
    abrir.abrirSerializado("sample.xml");
    verify(JOptionPane).showMessageDialog(anyObject(), anyString(), anyString(), anyInt());
  }
  
  @Test
  public void testAbrirXMLWithFileEndsWithJawXml() {
    abrir.abrirXML("sample.jaw.xml");
    verify(form).setNomeArquivoSalvo("sample.jaw.xml");
    verify(form).setEntidades(anyObject());
  }
  
  @Test
  public void testAbrirXMLWithFileNotEndsWithJawXml() {
    abrir.abrirXML("sample.xml");
    verify(JOptionPane).showMessageDialog(anyObject(), anyString(), anyString(), anyInt());
  }
  
  @Test
  public void testFiltro2AcceptWithDirectory() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(true);
    Filtro2 filtro2 = new Filtro2();
    assertEquals(true, filtro2.accept(file));
  }
  
  @Test
  public void testFiltro2AcceptWithFileNotEndsWithJawXml() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(true);
    when(file.getName()).thenReturn("sample.txt");
    Filtro2 filtro2 = new Filtro2();
    assertEquals(false, filtro2.accept(file));
  }
  
  @Test
  public void testFiltro2AcceptWithFileEndsWithJawXml() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(false);
    when(file.getName()).thenReturn("sample.jaw.xml");
    Filtro2 filtro2 = new Filtro2();
    assertEquals(true, filtro2.accept(file));
  }
  
  @Test
  public void testFiltro2GetDescription() {
    Filtro2 filtro2 = new Filtro2();
    assertEquals("XML JAW files", filtro2.getDescription());
  }
  
  @Test
  public void testFiltroAcceptWithDirectory() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(true);
    Filtro filtro = new Filtro();
    assertEquals(true, filtro.accept(file));
  }
  
  @Test
  public void testFiltroAcceptWithFileNotEndsWithJaw() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(true);
    when(file.getName()).thenReturn("sample.txt");
    Filtro filtro = new Filtro();
    assertEquals(false, filtro.accept(file));
  }
  
  @Test
  public void testFiltroAcceptWithFileEndsWithJaw() {
    File file = mock(File.class);
    when(file.isDirectory()).thenReturn(false);
    when(file.getName()).thenReturn("sample.jaw");
    Filtro filtro = new Filtro();
    assertEquals(true, filtro.accept(file));
  }
  
  @Test
  public void testFiltroGetDescription() {
    Filtro filtro = new Filtro();
    assertEquals("JAW files", filtro.getDescription());
  }
}