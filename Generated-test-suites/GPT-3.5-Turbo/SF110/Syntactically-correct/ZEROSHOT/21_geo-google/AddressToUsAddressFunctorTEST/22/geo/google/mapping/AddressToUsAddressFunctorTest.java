import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    private AddressToUsAddressFunctor addressToUsAddressFunctor;

    @Before
    public void setUp() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();
    }

    @Test
    public void testExecute_CountryNotNull() {
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);

        GeoUsAddress expectedUsAddress = new GeoUsAddress();
        expectedUsAddress.setCountry("USA");

        GeoUsAddress result = addressToUsAddressFunctor.execute(addr);

        assertEquals(expectedUsAddress, result);
    }

    @Test
    public void testExecute_AdministrativeAreaNotNull() {
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);

        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("California");
        names.add(areaName);
        adminArea.setAdministrativeAreaName(names);

        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);

        GeoUsAddress expectedUsAddress = new GeoUsAddress();
        expectedUsAddress.setCountry("USA");
        expectedUsAddress.setState("California");

        GeoUsAddress result = addressToUsAddressFunctor.execute(addr);

        assertEquals(expectedUsAddress, result);
    }

    // ... continue testing other methods ...

}