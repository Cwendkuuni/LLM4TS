import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertNotNull(listPopulation);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(new ArrayList<>());
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome(new ArrayList<>()));
        chromosomeList.add(new Chromosome(new ArrayList<>()));
        listPopulation.addChromosomes(chromosomeList);
        assertEquals(2, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().containsAll(chromosomeList));
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome(new ArrayList<>());
        Chromosome chromosome2 = new Chromosome(new ArrayList<>());
        chromosome1.setFitness(10);
        chromosome2.setFitness(15);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testIterator() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome(new ArrayList<>());
        listPopulation.addChromosome(chromosome);
        int count = 0;
        for (Chromosome c : listPopulation) {
            assertEquals(chromosome, c);
            count++;
        }
        assertEquals(1, count);
    }

}