import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        ListPopulation popul = new ListPopulation(10);
        assert(popul.getPopulationLimit() == 10);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation popul = new ListPopulation(chromosomes, 10);
        assert(popul.getPopulationLimit() == 10);
        assert(popul.getChromosomes().size() == 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        ListPopulation popul = new ListPopulation(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation popul = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation popul = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        popul.setChromosomes(chromosomes);
        assert(popul.getChromosomes().size() == 2);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNull() {
        ListPopulation popul = new ListPopulation(10);
        popul.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceedingPopulationLimit() {
        ListPopulation popul = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        popul.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation popul = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        popul.addChromosomes(chromosomes);
        assert(popul.getChromosomes().size() == 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithExceedingPopulationLimit() {
        ListPopulation popul = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        popul.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation popul = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        popul.setChromosomes(chromosomes);
        assert(popul.getChromosomes().size() == 2);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation popul = new ListPopulation(10);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        popul.addChromosome(chromosome1);
        popul.addChromosome(chromosome2);
        assert(popul.getFittestChromosome() == chromosome2);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation popul = new ListPopulation(10);
        assert(popul.getPopulationLimit() == 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        ListPopulation popul = new ListPopulation(10);
        popul.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithNewLimitSmallerThanCurrentSize() {
        ListPopulation popul = new ListPopulation(10);
        popul.addChromosome(new Chromosome());
        popul.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation popul = new ListPopulation(10);
        popul.addChromosome(new Chromosome());
        popul.addChromosome(new Chromosome());
        assert(popul.getPopulationSize() == 2);
    }
}